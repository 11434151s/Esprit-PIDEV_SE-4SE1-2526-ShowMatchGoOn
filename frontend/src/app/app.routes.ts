import { Routes } from '@angular/router';
import { FeedbackComponent } from './components/feedback/feedback.component';
import { WatchPartyComponent } from './components/watchparty/watchparty.component';

export const routes: Routes = [
  { path: 'feedback', component: FeedbackComponent },
  { path: 'watchparty', component: WatchPartyComponent }
];