import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { FeedbackService } from '../../services/feedback.service';

@Component({
  selector: 'app-feedback',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit, OnChanges {

  @Input() mode: 'user' | 'admin' = 'user';

  note: number | null = null;
  commentaire: string = '';
  watchPartyId: string = '';
  feedbacks: any[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  editingId: string | null = null;
  editNote: number | null = null;
  editCommentaire: string = '';

  constructor(private feedbackService: FeedbackService) {}

  ngOnInit(): void {
    if (this.mode === 'admin') {
      this.loadFeedbacks();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mode'] && this.mode === 'admin') {
      this.loadFeedbacks();
    }
  }

  loadFeedbacks(): void {
    this.feedbackService.getAll().subscribe({
      next: (data) => {
        this.feedbacks = data;
        this.errorMessage = '';
      },
      error: (err) => {
        console.error('Erreur chargement feedbacks', err);
        this.errorMessage = 'Impossible de charger les feedbacks.';
      }
    });
  }

  submit(form: NgForm): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (form.invalid) {
      form.control.markAllAsTouched();
      this.errorMessage = 'Veuillez corriger les erreurs du formulaire.';
      return;
    }

    const payload = {
      note: this.note!,
      commentaire: this.commentaire.trim(),
      watchPartyId: this.watchPartyId.trim()
    };

    this.feedbackService.addFeedback(payload).subscribe({
      next: () => {
        this.successMessage = 'Feedback ajouté avec succès.';
        this.errorMessage = '';
        form.resetForm();
        this.note = null;
        this.commentaire = '';
        this.watchPartyId = '';
      },
      error: (err) => {
        console.error('Erreur ajout feedback', err);
        this.errorMessage = err?.error?.commentaire || err?.error?.error || 'Erreur lors de l\'ajout du feedback.';
      }
    });
  }

  startEdit(feedback: any): void {
    this.editingId = feedback.id;
    this.editNote = feedback.note;
    this.editCommentaire = feedback.commentaire;
    this.successMessage = '';
    this.errorMessage = '';
  }

  cancelEdit(): void {
    this.editingId = null;
    this.editNote = null;
    this.editCommentaire = '';
  }

  saveEdit(id: string): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.editNote === null || this.editNote < 1 || this.editNote > 5) {
      this.errorMessage = 'La note doit être entre 1 et 5.';
      return;
    }

    if (!this.editCommentaire || this.editCommentaire.trim().length < 5) {
      this.errorMessage = 'Le commentaire doit contenir au moins 5 caractères.';
      return;
    }

    const payload = {
      note: this.editNote,
      commentaire: this.editCommentaire.trim()
    };

    this.feedbackService.updateFeedback(id, payload).subscribe({
      next: () => {
        this.successMessage = 'Feedback modifié avec succès.';
        this.cancelEdit();
        this.loadFeedbacks();
      },
      error: (err) => {
        console.error('Erreur modification feedback', err);
        this.errorMessage = err?.error?.commentaire || err?.error?.error || 'Erreur lors de la modification du feedback.';
      }
    });
  }

  deleteFeedback(id: string): void {
    this.successMessage = '';
    this.errorMessage = '';

    const confirmed = confirm('Voulez-vous vraiment supprimer ce feedback ?');
    if (!confirmed) return;

    this.feedbackService.deleteFeedback(id).subscribe({
      next: () => {
        this.successMessage = 'Feedback supprimé avec succès.';
        this.loadFeedbacks();
      },
      error: (err) => {
        console.error('Erreur suppression feedback', err);
        this.errorMessage = err?.error?.error || 'Erreur lors de la suppression du feedback.';
      }
    });
  }
}