import {Component, Input} from '@angular/core';
import {Flight} from "../data/flight";
import {Router} from "@angular/router";


@Component({
  selector: 'app-flight-list-item',
  templateUrl: './flight-list-item.component.html',
  styleUrl: './flight-list-item.component.css'
})
export class FlightListItemComponent {

  @Input()
  flight!: Flight;

  constructor(private router: Router) {
  }

  navigateToDetails() {
    this.router.navigate(['/flights', this.flight.flightNumber]);
  }
}
