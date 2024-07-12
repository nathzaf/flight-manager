import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Category} from "../data/category";
import {Airfield} from "../data/airfield";
import {Plane} from "../data/plane";
import {Passenger} from "../data/passenger";
import {CategoryService} from "../service/impl/category.service";
import {AirfieldService} from "../service/impl/airfield.service";
import {PlaneService} from "../service/impl/plane.service";
import {PassengerService} from "../service/impl/passenger.service";

@Component({
  selector: 'app-flight-search-filters',
  templateUrl: './flight-search-filters.component.html',
  styleUrl: './flight-search-filters.component.css'
})
export class FlightSearchFiltersComponent implements OnInit {

  categories: Category[] = [];

  airfields: Airfield[] = [];

  planes: Plane[] = [];

  pilots: Passenger[] = [];

  currentCategory: string = "";
  currentDeparture: string = "";
  currentArrival: string = "";
  currentPlane: string = "";
  currentPilot: string = "";
  currentStartDate: string = "";
  currentEndDate: string = "";

  @Output()
  reloadFlights = new EventEmitter<string>();

  @Output()
  searchByFlightNumber = new EventEmitter<string>();

  constructor(private categoryService: CategoryService,
              private airfieldService: AirfieldService,
              private planeService: PlaneService,
              private passengerService: PassengerService) {
  }

  ngOnInit(): void {
    this.categoryService.getAll().subscribe(
      categories => this.categories = categories
    );
    this.airfieldService.getAll().subscribe(
      airfields => this.airfields = airfields
    );
    this.planeService.getAll().subscribe(
      planes => this.planes = planes
    );
    this.passengerService.getPilots().subscribe(
      pilots => this.pilots = pilots
    );
  }

  reset() {
    window.location.reload();
  }

  buildRequestFilter(): void {
    let req = "?";
    if (this.currentCategory != "") {
      req += "category=" + this.currentCategory + "&";
    }
    if (this.currentDeparture != "") {
      req += "departure=" + this.currentDeparture + "&";
    }
    if (this.currentArrival != "") {
      req += "arrival=" + this.currentArrival + "&";
    }
    if (this.currentPlane != "") {
      req += "plane=" + this.currentPlane + "&";
    }
    if (this.currentPilot != "") {
      req += "pilot=" + this.currentPilot + "&";
    }
    if (this.currentStartDate != "") {
      req += "startDate=" + this.currentStartDate + "&";
    }
    if (this.currentEndDate != "") {
      req += "endDate=" + this.currentEndDate + "&";
    }
    this.reloadFlights.next(req);
  }

  onCategoryChange($event: Event) {
    this.currentCategory = ($event.target as HTMLSelectElement).value;
    this.buildRequestFilter();
  }

  onDepartureChange($event: Event) {
    this.currentDeparture = ($event.target as HTMLSelectElement).value;
    this.buildRequestFilter();
  }

  onArrivalChange($event: Event) {
    this.currentArrival = ($event.target as HTMLSelectElement).value;
    this.buildRequestFilter();
  }

  onPlaneChange($event: Event) {
    this.currentPlane = ($event.target as HTMLSelectElement).value;
    this.buildRequestFilter();
  }

  onPilotChange($event: Event) {
    this.currentPilot = ($event.target as HTMLSelectElement).value;
    this.buildRequestFilter();
  }

  onStartDateChange($event: Event) {
    this.currentStartDate = ($event.target as HTMLSelectElement).value;
    this.buildRequestFilter()
  }

  onEndDateChange($event: Event) {
    this.currentEndDate = ($event.target as HTMLSelectElement).value;
    this.buildRequestFilter()
  }

  onInput($event: Event) {
    this.searchByFlightNumber.emit(($event.target as HTMLInputElement).value);
  }
}
