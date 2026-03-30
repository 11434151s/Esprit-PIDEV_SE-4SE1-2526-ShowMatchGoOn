import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, MapPin, Building, Calendar, Plus } from 'lucide-angular';

@Component({
  selector: 'app-admin-cinema',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './admin-cinema.component.html',
  styleUrls: ['./admin-cinema.component.css'],
})
export class AdminCinemaComponent {
  readonly MapPinIcon = MapPin;
  readonly BuildingIcon = Building;
  readonly CalendarIcon = Calendar;
  readonly PlusIcon = Plus;
}
