import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlightsMenuComponent} from "./flights-menu/flights-menu.component";
import {FlightInfoComponent} from "./flight-info/flight-info.component";
import {CreateFlightComponent} from "./create-flight/create-flight.component";
import {PlanesMenuComponent} from "./planes-menu/planes-menu.component";
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";

const routes: Routes = [
  {path: '', component: FlightsMenuComponent},
  {path: 'flights', component: FlightsMenuComponent},
  {path: 'flights/:id', component: FlightInfoComponent},
  {path: 'create-flight', component: CreateFlightComponent},
  {path: 'planes', component: PlanesMenuComponent},
  {path: '404', component: PageNotFoundComponent},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
