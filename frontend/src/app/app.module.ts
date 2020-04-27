import {NgModule, ModuleWithProviders} from '@angular/core'
import {BrowserModule} from '@angular/platform-browser'
import {Route, RouterModule} from '@angular/router'

import {AppComponent} from './app.component'
import {HomeComponent} from './view/home/home.component'
import {AboutComponent} from './view/about/about.component'
import {NavMenuComponent} from './view/navmenu/navmenu.component'
import '../themer.ts'
import {ExtAngularModernModule} from '@sencha/ext-angular-modern';
// import { PatientsComponent } from './view/patients/patients.component';
import {PatientListComponent} from './view/patients/list/patient-list.component';

// import { ExtPanelComponent } from '@sencha/ext-angular-modern/lib/ExtPanel';
// import { ExtContainerComponent } from '@sencha/ext-angular-modern/lib/ExtContainer';
// import { ExtTitlebarComponent } from '@sencha/ext-angular-modern/lib/ExtTitlebar';
// import { ExtButtonComponent } from '@sencha/ext-angular-modern/lib/ExtButton';
// import { ExtTreelistComponent } from '@sencha/ext-angular-modern/lib/ExtTreelist';
// import { ExtGridComponent } from '@sencha/ext-angular-modern/lib/ExtGrid';

const routes: Route[] = [
  {path: '', redirectTo: 'about', pathMatch: 'full'},
  {path: 'about', component: AboutComponent},
  {path: 'home', component: HomeComponent},
  {path: 'patient/list', component: PatientListComponent}
]
export const routingModule: ModuleWithProviders = RouterModule.forRoot(routes, {useHash: true});

@NgModule({
  imports: [
    BrowserModule,
    routingModule,
    ExtAngularModernModule
  ],
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    NavMenuComponent,
    // PatientsComponent,
    PatientListComponent,
    // ExtPanelComponent,
    // ExtContainerComponent,
    // ExtTitlebarComponent,
    // ExtButtonComponent,
    // ExtTreelistComponent,
    // ExtGridComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
