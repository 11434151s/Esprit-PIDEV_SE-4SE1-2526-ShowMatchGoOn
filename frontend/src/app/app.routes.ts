import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layouts/admin-layout.component';
import { UserLayoutComponent } from './layouts/user-layout.component';

/**
 * Application Routes Configuration
 * WHY: Defines all application routes with lazy loading
 * Implements proper navigation structure for the SPA
 */
export const routes: Routes = [
  // Admin panel with layout
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      {
        path: 'users',
        loadComponent: () => import('./components/admin-users/admin-users.component').then(m => m.AdminUsersComponent),
        data: { title: 'User Management' }
      },
      {
        path: 'content',
        loadComponent: () => import('./components/admin-content/admin-content.component').then(m => m.AdminContentComponent),
        data: { title: 'Content Management' }
      },
      {
        path: 'categories',
        loadComponent: () => import('./components/admin-categories/admin-categories.component').then(m => m.AdminCategoriesComponent),
        data: { title: 'Categories' }
      },
      {
        path: 'cinema',
        loadComponent: () => import('./components/admin-cinema/admin-cinema.component').then(m => m.AdminCinemaComponent),
        data: { title: 'Cinema Management' }
      },
      {
        path: 'notifications',
        loadComponent: () => import('./components/admin-notifications/admin-notifications.component').then(m => m.AdminNotificationsComponent),
        data: { title: 'Notifications' }
      },
      {
        path: '',
        redirectTo: 'content',
        pathMatch: 'full'
      }
    ]
  },

  // User panel with layout
  {
    path: 'user',
    component: UserLayoutComponent,
    children: [
      {
        path: 'home',
        loadComponent: () => import('./components/unified-home/unified-home.component').then(m => m.UnifiedHomeComponent),
        data: { title: 'Home' }
      },
      {
        path: 'discover',
        loadComponent: () => import('./components/ai-discovery/ai-discovery.component').then(m => m.AiDiscoveryComponent),
        data: { title: 'Discovery' }
      },
      {
        path: 'cinema',
        loadComponent: () => import('./components/cinema-journey/cinema-journey.component').then(m => m.CinemaJourneyComponent),
        data: { title: 'Cinema' }
      },
      {
        path: 'social',
        loadComponent: () => import('./components/social-overlay/social-overlay.component').then(m => m.SocialOverlayComponent),
        data: { title: 'Social' }
      },
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
      }
    ]
  },

  // Default route
  {
    path: '',
    redirectTo: '/admin',
    pathMatch: 'full'
  },

  // Wildcard route for 404
  {
    path: '**',
    redirectTo: '/admin'
  }
];

