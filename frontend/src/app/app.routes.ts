import { Routes } from '@angular/router';

export const routes: Routes = [
  // Admin routes
  {
    path: 'admin/abonnements',
    loadComponent: () =>
      import('./components/admin-abonnement/admin-abonnement.component')
        .then(m => m.AdminAbonnementComponent)
  },

  // User routes
  {
    path: 'abonnements',
    loadComponent: () =>
      import('./components/user-abonnement/user-abonnement.component')
        .then(m => m.UserAbonnementComponent)
  },

  // Redirect par défaut
  { path: '', redirectTo: 'abonnements', pathMatch: 'full' }
];