import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  trigger,
  style,
  transition,
  animate,
} from '@angular/animations';
import {
  LucideAngularModule,
  Home,
  Sparkles,
  Ticket,
  Users,
  LayoutDashboard,
  FileText,
  MapPin,
  Bell,
  UserCog,
  Menu,
  X,
  CreditCard,
  Award,
} from 'lucide-angular';

import { UnifiedHomeComponent } from './components/unified-home/unified-home.component';
import { AiDiscoveryComponent } from './components/ai-discovery/ai-discovery.component';
import { CinemaJourneyComponent } from './components/cinema-journey/cinema-journey.component';
import { SocialOverlayComponent } from './components/social-overlay/social-overlay.component';
import { AdminContentComponent } from './components/admin-content/admin-content.component';
import { AdminCinemaComponent } from './components/admin-cinema/admin-cinema.component';
import { AdminNotificationsComponent } from './components/admin-notifications/admin-notifications.component';
import { AdminUsersComponent } from './components/admin-users/admin-users.component';
import { AdminAbonnementComponent } from './components/admin-abonnement/admin-abonnement.component';
import { UserAbonnementComponent } from './components/user-abonnement/user-abonnement.component';
import { AdminFidelityComponent } from './components/admin-fidelity/admin-fidelity.component';
import { UserFidelityComponent } from './components/user-fidelity/user-fidelity.component';

type AppMode = 'user' | 'admin';
type UserTab = 'home' | 'discover' | 'cinema' | 'social' | 'abonnements' | 'fidelity';
type AdminTab = 'content' | 'cinema' | 'notifications' | 'users' | 'abonnements' | 'fidelity';

interface TabItem {
  id: string;
  label: string;
  icon: any;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    LucideAngularModule,
    UnifiedHomeComponent,
    AiDiscoveryComponent,
    CinemaJourneyComponent,
    SocialOverlayComponent,
    AdminContentComponent,
    AdminCinemaComponent,
    AdminNotificationsComponent,
    AdminUsersComponent,
    AdminAbonnementComponent,
    UserAbonnementComponent,
    AdminFidelityComponent,
    UserFidelityComponent,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('slideDown', [
      transition(':enter', [
        style({ height: 0, opacity: 0 }),
        animate('200ms ease-out', style({ height: '*', opacity: 1 })),
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ height: 0, opacity: 0 })),
      ]),
    ]),
  ],
})
export class AppComponent {
  readonly HomeIcon = Home;
  readonly SparklesIcon = Sparkles;
  readonly TicketIcon = Ticket;
  readonly UsersIcon = Users;
  readonly LayoutDashboardIcon = LayoutDashboard;
  readonly FileTextIcon = FileText;
  readonly MapPinIcon = MapPin;
  readonly BellIcon = Bell;
  readonly UserCogIcon = UserCog;
  readonly MenuIcon = Menu;
  readonly XIcon = X;
  readonly CreditCardIcon = CreditCard;
  readonly AwardIcon = Award;

  appMode = signal<AppMode>('user');
  userTab = signal<UserTab>('home');
  adminTab = signal<AdminTab>('content');
  mobileMenuOpen = signal(false);
  sidebarOpen = signal(true);

  readonly userTabs: TabItem[] = [
    { id: 'home',        label: 'Home',          icon: Home },
    { id: 'discover',    label: 'AI Discovery',  icon: Sparkles },
    { id: 'cinema',      label: 'Cinema',        icon: Ticket },
    { id: 'social',      label: 'Watch Parties', icon: Users },
    { id: 'abonnements', label: 'Abonnements',   icon: CreditCard },
    { id: 'fidelity',    label: 'Fidélité',      icon: Award },
  ];

  readonly adminTabs: TabItem[] = [
    { id: 'content',       label: 'Content Management', icon: FileText },
    { id: 'cinema',        label: 'Cinema Partners',    icon: MapPin },
    { id: 'notifications', label: 'Notifications',      icon: Bell },
    { id: 'users',         label: 'Users & Loyalty',    icon: UserCog },
    { id: 'abonnements',   label: 'Abonnements',        icon: CreditCard },
    { id: 'fidelity',      label: 'Fidélité',           icon: Award },
  ];

  toggleMode() {
    this.appMode.set(this.appMode() === 'user' ? 'admin' : 'user');
    this.mobileMenuOpen.set(false);
  }

  setUserTab(tab: string) {
    this.userTab.set(tab as UserTab);
    this.mobileMenuOpen.set(false);
  }

  setAdminTab(tab: string) {
    this.adminTab.set(tab as AdminTab);
  }

  toggleMobileMenu() {
    this.mobileMenuOpen.set(!this.mobileMenuOpen());
  }

  toggleSidebar() {
    this.sidebarOpen.set(!this.sidebarOpen());
  }
}