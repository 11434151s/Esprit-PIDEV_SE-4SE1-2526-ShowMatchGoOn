import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Bell, Mail, MessageSquare, Send } from 'lucide-angular';

@Component({
  selector: 'app-admin-notifications',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './admin-notifications.component.html',
  styleUrls: ['./admin-notifications.component.css'],
})
export class AdminNotificationsComponent {
  readonly BellIcon = Bell;
  readonly MailIcon = Mail;
  readonly MessageSquareIcon = MessageSquare;
  readonly SendIcon = Send;
}
