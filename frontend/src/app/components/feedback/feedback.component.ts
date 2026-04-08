import { Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { FeedbackService } from '../../services/feedback.service';
import { WatchpartyService } from '../../services/watchparty.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-feedback',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit, OnChanges, AfterViewChecked {

  @Input() mode: 'user' | 'admin' = 'user';
  @ViewChild('ratingChart') ratingChartRef!: ElementRef<HTMLCanvasElement>;

  note: number | null = null;
  commentaire: string = '';
  watchPartyId: string = '';
  feedbacks: any[] = [];
  watchParties: any[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  editingId: string | null = null;
  editNote: number | null = null;
  editCommentaire: string = '';

  stars: number[] = [1, 2, 3, 4, 5];
  hoveredStar: number = 0;
  editHoveredStar: number = 0;

  // 👇 NOUVEAU : participant check
  isParticipant: boolean = false;
  currentUserId: string = '';

  statsTotal: number = 0;
  statsMoyenne: number = 0;
  statsMeilleure: number = 0;
  statsPire: number = 0;
  statsRepartition: number[] = [0, 0, 0, 0, 0];
  starsArray: string = '';

  private chartInstance: Chart | null = null;
  private chartRendered = false;

  constructor(
    private feedbackService: FeedbackService,
    private watchPartyService: WatchpartyService
  ) {}

  ngOnInit(): void {
    // Récupérer l'userId depuis le token JWT stocké
    this.currentUserId = this.extractUserIdFromToken();

    if (this.mode === 'admin') {
      this.loadFeedbacks();
    }
    if (this.mode === 'user') {
      this.loadWatchParties();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mode']?.currentValue === 'admin') this.loadFeedbacks();
    if (changes['mode']?.currentValue === 'user') this.loadWatchParties();
  }

  ngAfterViewChecked(): void {
    if (this.statsTotal > 0 && !this.chartRendered && this.ratingChartRef?.nativeElement) {
      this.chartRendered = true;
      this.renderChart();
    }
  }

  // 👇 Extraire l'userId depuis le JWT (adapter selon ton AuthService)
  private extractUserIdFromToken(): string {
    try {
      const token = localStorage.getItem('token') || localStorage.getItem('authToken') || '';
      if (!token) return '';
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub || payload.userId || payload.id || '';
    } catch {
      return '';
    }
  }

  // 👇 NOUVEAU : quand l'user change la WatchParty sélectionnée
  onWatchPartyChange(): void {
    this.isParticipant = false;
    this.feedbacks = [];
    this.errorMessage = '';

    if (!this.watchPartyId) return;

    // Charger les feedbacks de cette WatchParty
    this.feedbackService.getByWatchParty(this.watchPartyId).subscribe({
      next: (data) => {
        this.feedbacks = data;
        this.computeStats();
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les feedbacks.';
      }
    });

    // Vérifier si l'user courant est participant
    this.watchPartyService.getParticipants(this.watchPartyId).subscribe({
      next: (participants: string[]) => {
        this.isParticipant = participants.includes(this.currentUserId);
      },
      error: () => {
        this.isParticipant = false;
      }
    });
  }

  // ===== LIKE / DISLIKE (via API) =====
  vote(feedbackId: string, type: 'like' | 'dislike'): void {
    const call = type === 'like'
      ? this.feedbackService.likeFeedback(feedbackId)
      : this.feedbackService.dislikeFeedback(feedbackId);

    call.subscribe({
      next: (updated) => {
        const idx = this.feedbacks.findIndex(f => f.id === feedbackId);
        if (idx !== -1) this.feedbacks[idx] = updated;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du vote.';
      }
    });
  }

  // ===== ÉTOILES =====
  setNote(star: number): void { this.note = star; }
  hoverStar(star: number): void { this.hoveredStar = star; }
  resetHover(): void { this.hoveredStar = 0; }
  isStarActive(star: number): boolean { return star <= (this.hoveredStar || this.note || 0); }

  setEditNote(star: number): void { this.editNote = star; }
  hoverEditStar(star: number): void { this.editHoveredStar = star; }
  resetEditHover(): void { this.editHoveredStar = 0; }
  isEditStarActive(star: number): boolean { return star <= (this.editHoveredStar || this.editNote || 0); }

  // ===== DATA =====
  loadWatchParties(): void {
    this.watchPartyService.getAll().subscribe({
      next: (data) => { this.watchParties = data; },
      error: (err) => { console.error('Erreur chargement watchparties', err); }
    });
  }

  loadFeedbacks(): void {
    this.feedbackService.getAll().subscribe({
      next: (data) => {
        this.feedbacks = data;
        this.errorMessage = '';
        this.computeStats();
      },
      error: () => { this.errorMessage = 'Impossible de charger les feedbacks.'; }
    });
  }

  computeStats(): void {
    this.chartRendered = false;
    if (this.feedbacks.length === 0) {
      this.statsTotal = 0; this.statsMoyenne = 0;
      this.statsMeilleure = 0; this.statsPire = 0;
      this.statsRepartition = [0,0,0,0,0]; this.starsArray = '';
      return;
    }
    const notes = this.feedbacks.map(f => f.note);
    this.statsTotal = this.feedbacks.length;
    const sum = notes.reduce((a, b) => a + b, 0);
    this.statsMoyenne = parseFloat((sum / notes.length).toFixed(1));
    this.statsMeilleure = Math.max(...notes);
    this.statsPire = Math.min(...notes);
    this.statsRepartition = [1,2,3,4,5].map(n => notes.filter(v => v === n).length);
    const rounded = Math.round(this.statsMoyenne);
    this.starsArray = '★'.repeat(rounded) + '☆'.repeat(5 - rounded);
  }

  renderChart(): void {
    const canvas = this.ratingChartRef?.nativeElement;
    if (!canvas) return;
    if (this.chartInstance) { this.chartInstance.destroy(); this.chartInstance = null; }
    this.chartInstance = new Chart(canvas, {
      type: 'bar',
      data: {
        labels: ['1 ★','2 ★','3 ★','4 ★','5 ★'],
        datasets: [{ data: this.statsRepartition,
          backgroundColor: ['#E24B4A','#EF9F27','#888780','#1D9E75','#8B5CF6'],
          borderRadius: 6, borderWidth: 0 }]
      },
      options: { responsive: true, maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          y: { beginAtZero: true, ticks: { stepSize: 1, color: '#9CA3AF' }, grid: { color: 'rgba(139,92,246,0.1)' } },
          x: { ticks: { color: '#9CA3AF' }, grid: { display: false } }
        }
      }
    });
  }

  // ===== FORM =====
  submit(form: NgForm): void {
    this.successMessage = '';
    this.errorMessage = '';
    if (form.invalid || !this.note) {
      form.control.markAllAsTouched();
      this.errorMessage = 'Veuillez corriger les erreurs du formulaire.';
      return;
    }
    this.feedbackService.addFeedback({
      note: this.note!, commentaire: this.commentaire.trim(), watchPartyId: this.watchPartyId
    }).subscribe({
      next: () => {
        this.successMessage = 'Feedback ajouté avec succès.';
        form.resetForm(); this.note = null; this.hoveredStar = 0;
        this.commentaire = ''; this.watchPartyId = '';
        this.isParticipant = false; this.feedbacks = [];
      },
      error: (err) => {
        this.errorMessage = err?.error?.error || 'Erreur lors de l\'ajout.';
      }
    });
  }

  startEdit(feedback: any): void {
    this.editingId = feedback.id; this.editNote = feedback.note;
    this.editCommentaire = feedback.commentaire; this.editHoveredStar = 0;
    this.successMessage = ''; this.errorMessage = '';
  }

  cancelEdit(): void {
    this.editingId = null; this.editNote = null;
    this.editCommentaire = ''; this.editHoveredStar = 0;
  }

  saveEdit(id: string): void {
    this.successMessage = ''; this.errorMessage = '';
    if (!this.editNote || this.editNote < 1 || this.editNote > 5) {
      this.errorMessage = 'La note doit être entre 1 et 5.'; return;
    }
    if (!this.editCommentaire || this.editCommentaire.trim().length < 5) {
      this.errorMessage = 'Le commentaire doit faire au moins 5 caractères.'; return;
    }
    this.feedbackService.updateFeedback(id, { note: this.editNote, commentaire: this.editCommentaire.trim() })
      .subscribe({
        next: () => {
          this.successMessage = 'Feedback modifié.';
          this.cancelEdit(); this.loadFeedbacks();
        },
        error: (err) => { this.errorMessage = err?.error?.error || 'Erreur modification.'; }
      });
  }

  deleteFeedback(id: string): void {
    if (!confirm('Supprimer ce feedback ?')) return;
    this.feedbackService.deleteFeedback(id).subscribe({
      next: () => { this.successMessage = 'Feedback supprimé.'; this.loadFeedbacks(); },
      error: (err) => { this.errorMessage = err?.error?.error || 'Erreur suppression.'; }
    });
  }
}