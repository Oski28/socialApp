import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {AboutComponent} from './components/about/about.component';
import {FaqComponent} from './components/faq/faq.component';
import {SignupComponent} from './components/signup/signup.component';
import {TermsComponent} from './components/terms/terms.component';
import {SigninComponent} from './components/signin/signin.component';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {AuthGuard} from './service/auth.guard';
import {ProfileComponent} from './components/profil/profile.component';
import {FindUserComponent} from './components/find-user/find-user.component';
import {UserShowComponent} from './components/user-show/user-show.component';
import {CreateUserComponent} from './components/create-user/create-user.component';
import {AddCategoryComponent} from './components/add-category/add-category.component';
import {ShowCategoriesComponent} from './components/show-categories/show-categories.component';
import {AddEventComponent} from './components/add-event/add-event.component';
import {FindEventComponent} from './components/find-event/find-event.component';
import {ShowMyEventsComponent} from './components/show-my-events/show-my-events.component';
import {ShowJoinedEventsComponent} from './components/show-joined-events/show-joined-events.component';
import {ShowRequestToJoinComponent} from './components/show-request-to-join/show-request-to-join.component';
import {EditEventComponent} from './components/edit-event/edit-event.component';
import {ShowNoticeComponent} from './components/show-notice/show-notice.component';
import {ShowNoticesComponent} from './components/show-notices/show-notices.component';
import {ShowReportComponent} from './components/show-report/show-report.component';
import {ShowReportsComponent} from './components/show-reports/show-reports.component';
import {ShowChatComponent} from './components/show-chat/show-chat.component';
import {ShowChatsComponent} from './components/show-chats/show-chats.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {ShowEventComponent} from './components/show-event/show-event.component';
import {RoleGuardService} from './service/role-guard.service';
import {ForbiddenComponent} from './components/forbidden/forbidden.component';
import {ResetPasswordComponent} from './components/reset-password/reset-password.component';


const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'about', component: AboutComponent},
  {path: 'forbidden', component: ForbiddenComponent},
  {path: 'faq', component: FaqComponent},
  {path: 'password', component: ResetPasswordComponent},
  {path: 'password/:token', component: ResetPasswordComponent},
  {path: 'signup', component: SignupComponent},
  {path: 'signup/:confirm', component: SignupComponent},
  {path: 'terms', component: TermsComponent},
  {path: 'signin', component: SigninComponent},
  {path: 'signin/:guard', component: SigninComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'dashboard/:guard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'profil', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'chats', component: ShowChatsComponent, canActivate: [AuthGuard]},
  {path: 'userShow/:id', component: UserShowComponent, canActivate: [AuthGuard]},
  {path: 'editEvent/:id', component: EditEventComponent, canActivate: [AuthGuard]},
  {path: 'notice/:id', component: ShowNoticeComponent, canActivate: [AuthGuard]},
  {path: 'event/:id', component: ShowEventComponent, canActivate: [AuthGuard]},
  {
    path: 'report/:id', component: ShowReportComponent, canActivate: [RoleGuardService],
    data: {
      expectedRoleMod: 'Mod', expectedRoleAdmin: 'Admin'
    }
  },
  {path: 'chat/:id', component: ShowChatComponent, canActivate: [AuthGuard]},
  {path: 'findUser', component: FindUserComponent, canActivate: [AuthGuard]},
  {
    path: 'addUser', component: CreateUserComponent, canActivate: [RoleGuardService],
    data: {
      expectedRoleAdmin: 'Admin'
    }
  },
  {
    path: 'addCategory', component: AddCategoryComponent, canActivate: [RoleGuardService],
    data: {
      expectedRoleMod: 'Mod', expectedRoleAdmin: 'Admin'
    }
  },
  {
    path: 'showCategories', component: ShowCategoriesComponent, canActivate: [RoleGuardService],
    data: {
      expectedRoleMod: 'Mod', expectedRoleAdmin: 'Admin'
    }
  },
  {path: 'noticesShow', component: ShowNoticesComponent, canActivate: [AuthGuard]},
  {
    path: 'reportsShow', component: ShowReportsComponent, canActivate: [RoleGuardService],
    data: {
      expectedRoleMod: 'Mod', expectedRoleAdmin: 'Admin'
    }
  },
  {path: 'addEvent', component: AddEventComponent, canActivate: [AuthGuard]},
  {path: 'findEvent', component: FindEventComponent, canActivate: [AuthGuard]},
  {path: 'showMyEvents', component: ShowMyEventsComponent, canActivate: [AuthGuard]},
  {path: 'showJoined', component: ShowJoinedEventsComponent, canActivate: [AuthGuard]},
  {path: 'showRequest', component: ShowRequestToJoinComponent, canActivate: [AuthGuard]},
  {path: '**', component: NotFoundComponent},
  {path: 'notfound', component: NotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuard]
})
export class AppRoutingModule {
}
