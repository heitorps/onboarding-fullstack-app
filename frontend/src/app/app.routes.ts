import { Routes } from '@angular/router';

import { Auth } from './pages/auth/auth'
import { Timeline } from './pages/timeline/timeline';
import { Profile } from './pages/profile/profile';
import { authGuard } from './services/auth.guard';
import { ReviewDetailed } from './pages/review-detailed/review-detailed';

export const routes: Routes = [
    {path: '', component: Auth, pathMatch: 'full'},
    {path: 'timeline', component: Timeline, canActivate: [authGuard]},
    {path: 'profile/:id', component: Profile, canActivate: [authGuard]},
    {path: 'review/:reviewId', component: ReviewDetailed, canActivate: [authGuard]},
    {path: '**', redirectTo: ''}
];
