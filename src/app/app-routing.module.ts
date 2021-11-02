import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {HomeComponent} from './components/home/home.component';
import {AboutComponent} from './components/about/about.component';
import {FaqComponent} from './components/faq/faq.component';
import {SignupComponent} from './components/signup/signup.component';
import {TermsComponent} from './components/terms/terms.component';
import {SigninComponent} from './components/signin/signin.component';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {AuthGuard} from './service/auth.guard';
import {ProfileComponent} from './components/profil/profile.component';


const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'home', component: HomeComponent},
  {path: 'about', component: AboutComponent},
  {path: 'faq', component: FaqComponent},
  {path: 'signup', component: SignupComponent},
  {path: 'signup/:confirm', component: SignupComponent},
  {path: 'terms', component: TermsComponent},
  {path: 'signin', component: SigninComponent},
  {path: 'profil', component: ProfileComponent},
  {path: 'signin/:guard', component: SigninComponent},
  {path: '**', component: NotFoundComponent},
  {path: 'basic-ui', loadChildren: () => import('./basic-ui/basic-ui.module').then(m => m.BasicUiModule)},
  {path: 'charts', loadChildren: () => import('./charts/charts.module').then(m => m.ChartsDemoModule)},
  {path: 'forms', loadChildren: () => import('./forms/form.module').then(m => m.FormModule)},
  {path: 'tables', loadChildren: () => import('./tables/tables.module').then(m => m.TablesModule)},
  {path: 'icons', loadChildren: () => import('./icons/icons.module').then(m => m.IconsModule)},
  {path: 'general-pages', loadChildren: () => import('./general-pages/general-pages.module').then(m => m.GeneralPagesModule)},
  {path: 'apps', loadChildren: () => import('./apps/apps.module').then(m => m.AppsModule)},
  {path: 'user-pages', loadChildren: () => import('./user-pages/user-pages.module').then(m => m.UserPagesModule)},
  {path: 'error-pages', loadChildren: () => import('./error-pages/error-pages.module').then(m => m.ErrorPagesModule)},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuard]
})
export class AppRoutingModule {
}
