import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {TopBarComponent} from "./top-bar/top-bar.components";
import {FlightsMenuComponent} from './flights-menu/flights-menu.component';
import {FlightListItemComponent} from './flight-list-item/flight-list-item.component';
import {HttpClientModule} from "@angular/common/http";
import {AirfieldService} from "./service/impl/airfield.service";
import {BookingService} from "./service/impl/booking.service";
import {CategoryService} from "./service/impl/category.service";
import {FeedbackService} from "./service/impl/feedback.service";
import {FlightService} from "./service/impl/flight.service";
import {PassengerService} from "./service/impl/passenger.service";
import {PlaneService} from "./service/impl/plane.service";
import {FlightInfoComponent} from './flight-info/flight-info.component';
import {SweetAlert2Module} from "@sweetalert2/ngx-sweetalert2";
import {CreatePassengerComponent} from './create-passenger/create-passenger.component';
import {PlaneCardComponent} from './plane-card/plane-card.component';
import {FlightSearchFiltersComponent} from './flight-search-filters/flight-search-filters.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FlightDetailsComponent} from './flight-details/flight-details.component';
import {BookingComponent} from './booking/booking.component';
import {FeedbacksComponent} from './feedbacks/feedbacks.component';
import {CreateFeedbackComponent} from './create-feedback/create-feedback.component';
import {StarRatingComponent} from './star-rating/star-rating.component';
import {FeedbackCardComponent} from './feedback-card/feedback-card.component';
import {CreateFlightComponent} from './create-flight/create-flight.component';
import {PlanesMenuComponent} from './planes-menu/planes-menu.component';
import {CreatePlaneComponent} from './create-plane/create-plane.component';
import {SpinnerComponent} from './spinner/spinner.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    TopBarComponent,
    FlightsMenuComponent,
    FlightListItemComponent,
    FlightInfoComponent,
    CreatePassengerComponent,
    PlaneCardComponent,
    FlightSearchFiltersComponent,
    FlightDetailsComponent,
    BookingComponent,
    FeedbacksComponent,
    CreateFeedbackComponent,
    StarRatingComponent,
    FeedbackCardComponent,
    CreateFlightComponent,
    PlanesMenuComponent,
    CreatePlaneComponent,
    SpinnerComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    SweetAlert2Module,
    FormsModule
  ],
  providers: [
    AirfieldService,
    BookingService,
    CategoryService,
    FeedbackService,
    FlightService,
    PassengerService,
    PlaneService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
