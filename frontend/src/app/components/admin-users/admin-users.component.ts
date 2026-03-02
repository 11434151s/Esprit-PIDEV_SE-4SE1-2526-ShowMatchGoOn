import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, UserCog, Users, Award, TrendingUp } from 'lucide-angular';

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css'],
})
export class AdminUsersComponent {
  readonly UserCogIcon = UserCog;
  readonly UsersIcon = Users;
  readonly AwardIcon = Award;
  readonly TrendingUpIcon = TrendingUp;
}
