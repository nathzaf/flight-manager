import {Component, Input} from '@angular/core';
import {Flight} from "../data/flight";

@Component({
  selector: 'app-flight-details',
  templateUrl: './flight-details.component.html',
  styleUrl: './flight-details.component.css'
})
export class FlightDetailsComponent {

  @Input()
  flight!: Flight;

  @Input()
  isPassed: boolean = false;

}
