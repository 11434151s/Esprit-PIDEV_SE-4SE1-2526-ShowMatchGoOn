import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Ticket, MapPin, Calendar } from 'lucide-angular';

@Component({
  selector: 'app-cinema-journey',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './cinema-journey.component.html',
  styleUrls: ['./cinema-journey.component.css'],
})
export class CinemaJourneyComponent {
  readonly TicketIcon = Ticket;
  readonly MapPinIcon = MapPin;
  readonly CalendarIcon = Calendar;
}
