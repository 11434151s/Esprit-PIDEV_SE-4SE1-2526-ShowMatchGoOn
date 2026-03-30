import { Component, Input, OnChanges, OnInit, SimpleChanges, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { WatchpartyService } from '../../services/watchparty.service';

@Component({
  selector: 'app-watchparty',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './watchparty.component.html',
  styleUrls: ['./watchparty.component.css']
})
export class WatchPartyComponent implements OnInit, OnChanges {

  @Input() mode: 'user' | 'admin' = 'user';

  titre: string = '';
  contenuId: string = '';
  list: any[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  private service = inject(WatchpartyService);

  ngOnInit(): void {
    this.load();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mode']) {
      this.load();
    }
  }

  load(): void {
    this.service.getAll().subscribe({
      next: (data: any[]) => {
        this.list = data;
        this.errorMessage = '';
      },
      error: (err) => {
        console.error('Erreur chargement watch parties', err);
        this.errorMessage = 'Impossible de charger les watch parties.';
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
      titre: this.titre.trim(),
      contenuId: this.contenuId.trim()
    };

    this.service.add(payload).subscribe({
      next: () => {
        this.successMessage = 'WatchParty créée avec succès.';
        this.errorMessage = '';
        form.resetForm();
        this.titre = '';
        this.contenuId = '';
        this.load();
      },
      error: (err) => {
        console.error('Erreur création watch party', err);
        this.errorMessage = err?.error?.titre || err?.error?.contenuId || err?.error?.error || 'Erreur lors de la création de la watch party.';
      }
    });
  }

  joinWatchParty(id: string): void {
    this.successMessage = '';
    this.errorMessage = '';

    this.service.join(id).subscribe({
      next: () => {
        this.successMessage = 'Session rejointe avec succès.';
        this.load();
      },
      error: (err) => {
        console.error('Erreur join watch party', err);
        this.errorMessage = err?.error?.error || 'Erreur lors du join.';
      }
    });
  }

  leaveWatchParty(id: string): void {
    this.successMessage = '';
    this.errorMessage = '';

    this.service.leave(id).subscribe({
      next: () => {
        this.successMessage = 'Session quittée avec succès.';
        this.load();
      },
      error: (err) => {
        console.error('Erreur leave watch party', err);
        this.errorMessage = err?.error?.error || 'Erreur lors du leave.';
      }
    });
  }

  deleteWatchParty(id: string): void {
    if (!confirm('Voulez-vous vraiment supprimer cette WatchParty ?')) {
      return;
    }

    this.service.delete(id).subscribe({
      next: () => {
        this.successMessage = 'WatchParty supprimée avec succès.';
        this.load();
      },
      error: (err) => {
        console.error('Erreur suppression watch party', err);
        this.errorMessage = err?.error?.error || 'Erreur lors de la suppression.';
      }
    });
  }
}