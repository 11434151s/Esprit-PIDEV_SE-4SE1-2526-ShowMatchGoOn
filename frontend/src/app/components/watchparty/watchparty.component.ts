import { Component, Input, Output, EventEmitter, OnChanges, OnInit, OnDestroy, SimpleChanges, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { WatchpartyService } from '../../services/watchparty.service';

@Component({
  selector: 'app-watchparty',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './watchparty.component.html',
  styleUrls: ['./watchparty.component.css']
})
export class WatchPartyComponent implements OnInit, OnChanges, OnDestroy {

  @Input() mode: 'user' | 'admin' = 'user';
  @Output() onOpenSession = new EventEmitter<string>();

  titre: string = '';
  contenuId: string = '';
  list: any[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  createdWatchPartyId: string | null = null;
  inviteLink: string = '';
  linkCopied: boolean = false;

  // 🔔 Notifications d'approbation (côté créateur)
  pendingRequests: { userId: string; watchPartyId: string; watchPartyTitre: string; timestamp: number }[] = [];

  private service = inject(WatchpartyService);
  private router = inject(Router);
  private pollTimer: any;
  private readonly STORAGE_KEY = 'wp_join_requests';
  private readonly RESPONSE_KEY = 'wp_join_responses';

  // ID de l'utilisateur courant
  private currentUserId: string = 'user_' + Math.random().toString(36).slice(2, 8);

  ngOnInit(): void {
    this.load();
    this.startPollingRequests();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mode']) {
      this.load();
    }
  }

  ngOnDestroy(): void {
    clearInterval(this.pollTimer);
  }

  load(): void {
    this.service.getAll().subscribe({
      next: (data: any[]) => {
        this.list = data;
        this.errorMessage = '';
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les watch parties.';
      }
    });
  }

  submit(form: NgForm): void {
    this.successMessage = '';
    this.errorMessage = '';
    this.createdWatchPartyId = null;
    this.linkCopied = false;

    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    const payload = {
      titre: this.titre.trim(),
      contenuId: this.contenuId.trim()
    };

    this.service.add(payload).subscribe({
      next: (created: any) => {
        this.createdWatchPartyId = created.id;
        this.inviteLink = `${window.location.origin}/watchparty/${created.id}`;

        // Stocker l'ID du créateur pour cette WatchParty
        localStorage.setItem(`wp_creator_${created.id}`, this.currentUserId);

        form.resetForm();
        this.titre = '';
        this.contenuId = '';
        this.load();

        // ✅ Rediriger le créateur directement vers la session
        this.router.navigate(['/watchparty', created.id]);

        setTimeout(() => {
          this.createdWatchPartyId = null;
        }, 30000);
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.error || 'Erreur lors de la création.';
      }
    });
  }

  copyLink(): void {
    navigator.clipboard.writeText(this.inviteLink).then(() => {
      this.linkCopied = true;
      setTimeout(() => this.linkCopied = false, 3000);
    });
  }

  closeInvite(): void {
    this.createdWatchPartyId = null;
    this.linkCopied = false;
  }

  // ✅ Join → envoie une demande d'approbation + navigue vers la session en attente
  joinWatchParty(watchParty: any): void {
    this.errorMessage = '';

    const request = {
      userId: this.currentUserId,
      watchPartyId: watchParty.id,
      watchPartyTitre: watchParty.titre,
      timestamp: Date.now(),
      status: 'pending'
    };

    // Écrire la demande dans localStorage
    const existing = this.getStoredRequests();
    const alreadyPending = existing.find(
      r => r.userId === this.currentUserId && r.watchPartyId === watchParty.id
    );
    if (!alreadyPending) {
      existing.push(request);
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(existing));
    }

    // ✅ Naviguer vers la session avec ?pending=userId
    this.router.navigate(['/watchparty', watchParty.id], {
      queryParams: { pending: this.currentUserId }
    });
  }

  leaveWatchParty(id: string): void {
    this.service.leave(id).subscribe({
      next: () => {
        this.successMessage = 'Session quittée avec succès.';
        this.load();
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.error || 'Erreur lors du leave.';
      }
    });
  }

  deleteWatchParty(id: string): void {
    if (!confirm('Voulez-vous vraiment supprimer cette WatchParty ?')) return;
    this.service.delete(id).subscribe({
      next: () => {
        this.successMessage = 'WatchParty supprimée avec succès.';
        this.load();
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.error || 'Erreur lors de la suppression.';
      }
    });
  }

  // 🔔 Polling : le créateur vérifie les nouvelles demandes
  private startPollingRequests(): void {
    this.pollTimer = setInterval(() => {
      const requests = this.getStoredRequests();
      this.pendingRequests = requests.filter(r => {
        const creatorId = localStorage.getItem(`wp_creator_${r.watchPartyId}`);
        return creatorId === this.currentUserId && r.status === 'pending';
      });
    }, 3000);
  }

  private getStoredRequests(): any[] {
    try {
      return JSON.parse(localStorage.getItem(this.STORAGE_KEY) || '[]');
    } catch {
      return [];
    }
  }

  // ✅ Créateur accepte un participant
  approveRequest(request: any): void {
    this.service.join(request.watchPartyId).subscribe({
      next: () => {
        const responses = this.getStoredResponses();
        responses.push({
          userId: request.userId,
          watchPartyId: request.watchPartyId,
          status: 'approved',
          timestamp: Date.now()
        });
        localStorage.setItem(this.RESPONSE_KEY, JSON.stringify(responses));
        this.markRequestHandled(request);
        this.load();
      },
      error: () => {
        this.errorMessage = "Erreur lors de l'approbation.";
      }
    });
  }

  // ❌ Créateur refuse un participant
  rejectRequest(request: any): void {
    const responses = this.getStoredResponses();
    responses.push({
      userId: request.userId,
      watchPartyId: request.watchPartyId,
      status: 'rejected',
      timestamp: Date.now()
    });
    localStorage.setItem(this.RESPONSE_KEY, JSON.stringify(responses));
    this.markRequestHandled(request);
  }

  private markRequestHandled(request: any): void {
    const existing = this.getStoredRequests();
    const updated = existing.map(r =>
      r.userId === request.userId && r.watchPartyId === request.watchPartyId
        ? { ...r, status: 'handled' }
        : r
    );
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(updated));
    this.pendingRequests = this.pendingRequests.filter(
      r => !(r.userId === request.userId && r.watchPartyId === request.watchPartyId)
    );
  }

  private getStoredResponses(): any[] {
    try {
      return JSON.parse(localStorage.getItem(this.RESPONSE_KEY) || '[]');
    } catch {
      return [];
    }
  }
}