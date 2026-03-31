 
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Crown, Star, Check, CreditCard } from 'lucide-angular';
import { AbonnementService } from '../../services/abonnement.service';
import { Abonnement } from '../../models/abonnement.model';

@Component({
  selector: 'app-user-abonnement',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './user-abonnement.component.html'
})
export class UserAbonnementComponent implements OnInit {
  readonly CrownIcon = Crown;
  readonly StarIcon = Star;
  readonly CheckIcon = Check;
  readonly CreditCardIcon = CreditCard;

  abonnements: Abonnement[] = [];
  loading = false;
  errorMessage = '';

  constructor(private service: AbonnementService) {}

  ngOnInit(): void {
    this.loading = true;
    this.service.getAll().subscribe({
      next: (data) => { this.abonnements = data; this.loading = false; },
      error: () => { this.errorMessage = 'Impossible de charger les offres.'; this.loading = false; }
    });
  }

  isPremium(a: Abonnement): boolean {
    return a.type === 'PREMIUM';
  }
}