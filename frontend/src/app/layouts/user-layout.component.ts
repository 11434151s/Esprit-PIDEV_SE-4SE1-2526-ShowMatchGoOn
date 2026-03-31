import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import {
  LucideAngularModule,
  Home,
  Sparkles,
  Ticket,
  Users,
  Menu,
  X,
  ArrowRight,
} from 'lucide-angular';
import { trigger, state, style, transition, animate } from '@angular/animations';

interface TabItem {
  id: string;
  label: string;
  icon: any;
  route: string;
}

@Component({
  selector: 'app-user-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, LucideAngularModule],
  template: `
    <div class="min-h-screen bg-[#0B0E14] text-white">
      <!-- Header -->
      <header class="sticky top-0 z-40 bg-[#0B0E14]/80 backdrop-blur-xl border-b border-[#8B5CF6]/20">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex items-center justify-between h-16">
            <!-- Logo -->
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 bg-gradient-to-br from-[#8B5CF6] to-[#EC4899] rounded-xl flex items-center justify-center">
                <lucide-icon [img]="TicketIcon" class="w-6 h-6 text-white"></lucide-icon>
              </div>
              <div>
                <h1 class="text-xl font-semibold bg-gradient-to-r from-[#8B5CF6] to-[#EC4899] bg-clip-text text-transparent">
                  ShowMatchGoOn
                </h1>
                <p class="text-xs text-gray-400">Your Entertainment Hub</p>
              </div>
            </div>

            <!-- Desktop Navigation -->
            <nav class="hidden md:flex items-center gap-6">
              <a
                *ngFor="let tab of userTabs"
                [routerLink]="tab.route"
                class="flex items-center gap-2 px-4 py-2 rounded-lg transition-all text-gray-400 hover:text-white hover:bg-[#8B5CF6]/10">
                <lucide-icon [img]="tab.icon" class="w-4 h-4"></lucide-icon>
                <span class="text-sm">{{ tab.label }}</span>
              </a>
            </nav>

            <!-- Mode Toggle & Mobile Menu -->
            <div class="flex items-center gap-3">
              <button
                (click)="switchToAdmin()"
                class="hidden md:flex items-center gap-2 px-4 py-2 border border-[#8B5CF6] text-[#8B5CF6] hover:bg-[#8B5CF6]/10 rounded-lg transition-all text-sm">
                <lucide-icon [img]="ArrowRightIcon" class="w-4 h-4"></lucide-icon>
                <span>Admin Panel</span>
              </button>
              <button
                (click)="toggleMobileMenu()"
                class="md:hidden p-2 text-gray-400 hover:text-white hover:bg-[#8B5CF6]/10 rounded-lg">
                <lucide-icon [img]="mobileMenuOpen() ? XIcon : MenuIcon" class="w-6 h-6"></lucide-icon>
              </button>
            </div>
          </div>
        </div>

        <!-- Mobile Menu -->
        <div
          *ngIf="mobileMenuOpen()"
          [@slideDown]
          class="md:hidden border-t border-[#8B5CF6]/20 overflow-hidden">
          <div class="px-4 py-4 space-y-2">
            <a
              *ngFor="let tab of userTabs"
              [routerLink]="tab.route"
              class="w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-all text-gray-400 hover:text-white hover:bg-[#8B5CF6]/10">
              <lucide-icon [img]="tab.icon" class="w-5 h-5"></lucide-icon>
              <span>{{ tab.label }}</span>
            </a>
            <button
              (click)="switchToAdmin()"
              class="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-gray-400 hover:text-white hover:bg-[#8B5CF6]/10 border-t border-[#8B5CF6]/20 mt-4 pt-4">
              <lucide-icon [img]="ArrowRightIcon" class="w-5 h-5"></lucide-icon>
              <span>Admin Panel</span>
            </button>
          </div>
        </div>
      </header>

      <!-- Main Content -->
      <main class="pb-12">
        <router-outlet></router-outlet>
      </main>

      <!-- Footer -->
      <footer class="border-t border-[#8B5CF6]/20 py-8">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex flex-col md:flex-row items-center justify-between gap-4">
            <div class="flex items-center gap-3">
              <div class="w-8 h-8 bg-gradient-to-br from-[#8B5CF6] to-[#EC4899] rounded-lg flex items-center justify-center">
                <lucide-icon [img]="TicketIcon" class="w-5 h-5 text-white"></lucide-icon>
              </div>
              <div>
                <p class="text-sm text-white">ShowMatchGoOn</p>
                <p class="text-xs text-gray-400">© 2026 All rights reserved</p>
              </div>
            </div>
            <div class="flex items-center gap-6 text-sm text-gray-400">
              <a href="#" class="hover:text-[#8B5CF6] transition-colors">
                Privacy Policy
              </a>
              <a href="#" class="hover:text-[#8B5CF6] transition-colors">
                Terms of Service
              </a>
              <a href="#" class="hover:text-[#8B5CF6] transition-colors">
                Contact
              </a>
            </div>
          </div>
        </div>
      </footer>
    </div>
  `,
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
export class UserLayoutComponent {
  readonly HomeIcon = Home;
  readonly SparklesIcon = Sparkles;
  readonly TicketIcon = Ticket;
  readonly UsersIcon = Users;
  readonly MenuIcon = Menu;
  readonly XIcon = X;
  readonly ArrowRightIcon = ArrowRight;

  mobileMenuOpen = signal(false);

  readonly userTabs: TabItem[] = [
    { id: 'home', label: 'Home', icon: Home, route: '/user/home' },
    { id: 'discover', label: 'Discover', icon: Sparkles, route: '/user/discover' },
    { id: 'cinema', label: 'Cinema', icon: Ticket, route: '/user/cinema' },
    { id: 'social', label: 'Social', icon: Users, route: '/user/social' },
  ];

  constructor(private router: Router) {}

  toggleMobileMenu() {
    this.mobileMenuOpen.set(!this.mobileMenuOpen());
  }

  switchToAdmin() {
    this.router.navigate(['/admin']);
  }
}
