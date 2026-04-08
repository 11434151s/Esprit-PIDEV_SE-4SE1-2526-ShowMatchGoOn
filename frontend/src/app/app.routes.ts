import { Routes } from '@angular/router';
import { FeedbackComponent } from './components/feedback/feedback.component';
import { WatchPartyComponent } from './components/watchparty/watchparty.component';
import { WatchpartySessionComponent } from './components/watchparty-session/watchparty-session.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'feedback', component: FeedbackComponent },
  { path: 'watchparty', component: WatchPartyComponent },
  { path: 'watchparty/:id', component: WatchpartySessionComponent }
];