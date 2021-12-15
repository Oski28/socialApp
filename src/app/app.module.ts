import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ChartsModule, ThemeService} from 'ng2-charts';

import {AppComponent} from './app.component';
import {NavbarComponent} from './shared/navbar/navbar.component';
import {SidebarComponent} from './shared/sidebar/sidebar.component';
import {FooterComponent} from './shared/footer/footer.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SpinnerComponent} from './shared/spinner/spinner.component';
import {ContentAnimateDirective} from './shared/directives/content-animate.directive';
import {HomeComponent} from './components/home/home.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {AuthInterceptorService} from './service/auth-interceptor.service';
import {RxReactiveFormsModule} from '@rxweb/reactive-form-validators';
import {AboutComponent} from './components/about/about.component';
import {FaqComponent} from './components/faq/faq.component';
import {SignupComponent} from './components/signup/signup.component';
import {TermsComponent} from './components/terms/terms.component';
import {SigninComponent} from './components/signin/signin.component';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {ProfileComponent} from './components/profil/profile.component';
import {FindUserComponent} from './components/find-user/find-user.component';
import {NgxPaginationModule} from 'ngx-pagination';
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

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    SidebarComponent,
    FooterComponent,
    DashboardComponent,
    SpinnerComponent,
    ContentAnimateDirective,
    HomeComponent,
    AboutComponent,
    FaqComponent,
    SignupComponent,
    TermsComponent,
    SigninComponent,
    NotFoundComponent,
    ProfileComponent,
    FindUserComponent,
    UserShowComponent,
    CreateUserComponent,
    AddCategoryComponent,
    ShowCategoriesComponent,
    AddEventComponent,
    FindEventComponent,
    ShowMyEventsComponent,
    ShowJoinedEventsComponent,
    ShowRequestToJoinComponent,
    EditEventComponent,
    ShowNoticeComponent,
    ShowNoticesComponent,
    ShowReportComponent,
    ShowReportsComponent,
    ShowChatComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    ChartsModule,
    HttpClientModule,
    RxReactiveFormsModule,
    NgxPaginationModule,
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}, DatePipe, ThemeService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
