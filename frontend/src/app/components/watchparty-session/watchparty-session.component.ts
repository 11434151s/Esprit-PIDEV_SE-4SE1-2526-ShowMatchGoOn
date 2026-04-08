import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { WatchpartyService } from '../../services/watchparty.service';

interface ChatMessage {
  author: string;
  initials: string;
  text: string;
  time: string;
  isMe: boolean;
}

@Component({
  selector: 'app-watchparty-session',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './watchparty-session.component.html',
  styleUrls: ['./watchparty-session.component.css']
})
export class WatchpartySessionComponent implements OnInit, OnDestroy {

  @ViewChild('chatContainer') chatContainer!: ElementRef;
  @Input() sessionId: string = '';
  @Output() onClose = new EventEmitter<void>();

  session: any = null;
  loading = true;
  errorMessage = '';
  successMessage = '';
  activeTab: 'members' | 'chat' = 'members';
  chatInput = '';
  chatMessages: ChatMessage[] = [];
  sessionLinkCopied = false;

  // 🔐 État d'approbation
  approvalStatus: 'waiting' | 'approved' | 'rejected' | 'host' = 'host';
  pendingUserId: string | null = null;
  private readonly RESPONSE_KEY = 'wp_join_responses';

  memberColors = [
    { bg: 'rgba(124,92,252,0.25)', text: '#a78bfa' },
    { bg: 'rgba(34,211,160,0.2)',  text: '#22d3a0' },
    { bg: 'rgba(251,146,60,0.2)',  text: '#fb923c' },
    { bg: 'rgba(239,68,68,0.2)',   text: '#f87171' },
    { bg: 'rgba(59,130,246,0.2)',  text: '#60a5fa' },
  ];

  private pollTimer: any;
  private successTimer: any;
  public realSessionId: string = '';

  constructor(
    private watchpartyService: WatchpartyService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // ✅ Lire l'ID depuis la route en priorité, sinon depuis @Input
    const routeId = this.route.snapshot.paramMap.get('id');
    const pendingUser = this.route.snapshot.queryParamMap.get('pending');

    this.realSessionId = routeId || this.sessionId || '';

    if (!this.realSessionId) {
      this.errorMessage = 'Identifiant de session manquant.';
      this.loading = false;
      return;
    }

    if (pendingUser) {
      // 👤 Venu via bouton Join → en attente d'approbation
      this.pendingUserId = pendingUser;
      this.approvalStatus = 'waiting';
      this.loadSession();
      this.startPollingApproval();
    } else {
      // 👑 Venu via création ou URL directe → host
      this.approvalStatus = 'host';
      this.loadSession();
      this.startSessionPolling();
    }
  }

  ngOnDestroy(): void {
    clearInterval(this.pollTimer);
    clearTimeout(this.successTimer);
  }

  private loadSession(): void {
    this.watchpartyService.getById(this.realSessionId).subscribe({
      next: (data: any) => {
        this.session = data;
        this.loading = false;
        if (data.statut === 'CLOSED' || data.statut === 'CANCELLED') {
          this.errorMessage = 'Cette WatchParty est fermée ou annulée.';
        }
      },
      error: (err: any) => {
        this.loading = false;
        this.errorMessage = err?.status === 404
          ? "Cette WatchParty n'existe pas ou a été supprimée."
          : 'Impossible de charger la session.';
      }
    });
  }

  // 🔄 Polling : le participant vérifie si le créateur a répondu
  private startPollingApproval(): void {
    this.pollTimer = setInterval(() => {
      try {
        const responses = JSON.parse(localStorage.getItem(this.RESPONSE_KEY) || '[]');
        const myResponse = responses.find(
          (r: any) => r.userId === this.pendingUserId && r.watchPartyId === this.realSessionId
        );

        if (myResponse) {
          clearInterval(this.pollTimer);

          if (myResponse.status === 'approved') {
            this.approvalStatus = 'approved';
            // ✅ Rejoindre vraiment la session côté backend
            this.watchpartyService.join(this.realSessionId).subscribe({
              next: () => {
                this.watchpartyService.getById(this.realSessionId).subscribe((updated: any) => {
                  this.session = updated;
                  this.startSessionPolling();
                });
              },
              error: () => {
                this.errorMessage = 'Erreur lors du join.';
              }
            });
          } else {
            // ❌ Refusé
            this.approvalStatus = 'rejected';
          }

          // Nettoyer la réponse du localStorage
          const cleaned = responses.filter(
            (r: any) => !(r.userId === this.pendingUserId && r.watchPartyId === this.realSessionId)
          );
          localStorage.setItem(this.RESPONSE_KEY, JSON.stringify(cleaned));
        }
      } catch {}
    }, 3000);
  }

  // 🔄 Polling session : refresh les participants toutes les 10s
  private startSessionPolling(): void {
    clearInterval(this.pollTimer);
    this.pollTimer = setInterval(() => {
      this.watchpartyService.getById(this.realSessionId).subscribe({
        next: (data: any) => { this.session = data; },
        error: () => {}
      });
    }, 10000);
  }

  sendMessage(): void {
    const text = this.chatInput.trim();
    if (!text) return;

    const now = new Date();
    const time = now.getHours().toString().padStart(2, '0') + ':'
               + now.getMinutes().toString().padStart(2, '0');

    this.chatMessages.push({
      author: 'Vous',
      initials: 'YO',
      text,
      time,
      isMe: true
    });

    this.chatInput = '';

    setTimeout(() => {
      if (this.chatContainer?.nativeElement) {
        this.chatContainer.nativeElement.scrollTop =
          this.chatContainer.nativeElement.scrollHeight;
      }
    }, 50);
  }

  copySessionLink(): void {
    const link = `${window.location.origin}/watchparty/${this.realSessionId}`;
    navigator.clipboard.writeText(link).then(() => {
      this.sessionLinkCopied = true;
      setTimeout(() => (this.sessionLinkCopied = false), 3000);
    });
  }

  close(): void {
    this.onClose.emit();
    this.router.navigate(['/watchparty']);
  }

  private showSuccess(msg: string): void {
    this.successMessage = msg;
    clearTimeout(this.successTimer);
    this.successTimer = setTimeout(() => (this.successMessage = ''), 4000);
  }
}