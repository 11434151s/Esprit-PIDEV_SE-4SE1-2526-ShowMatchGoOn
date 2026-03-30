import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Users, Video, MessageCircle } from 'lucide-angular';

@Component({
  selector: 'app-social-overlay',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './social-overlay.component.html',
  styleUrls: ['./social-overlay.component.css'],
})
export class SocialOverlayComponent {
  readonly UsersIcon = Users;
  readonly VideoIcon = Video;
  readonly MessageCircleIcon = MessageCircle;
}
