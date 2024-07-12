import {Component, OnInit} from '@angular/core';
import {Flight} from "../data/flight";
import {FlightService} from "../service/impl/flight.service";

@Component({
  selector: 'app-flights-menu',
  templateUrl: './flights-menu.component.html',
  styleUrl: './flights-menu.component.css'
})
export class FlightsMenuComponent implements OnInit {

  flights: Flight[] = [];

  allFlights: Flight[] = [];

  currentSearchByFlightNumber: string = '';

  loading: boolean = false;

  constructor(private flightService: FlightService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.flightService.getAll().subscribe(
      flights => {
        this.flights = flights;
        this.allFlights = flights;
        this.searchByFlightNumber(this.currentSearchByFlightNumber);
        this.loading = false;
      }
    );
  }

  reloadFlights(req: string) {
    this.flightService.getFlightsByCriteria(req).subscribe(
      flights => {
        this.flights = flights;
        this.allFlights = flights;
        this.searchByFlightNumber(this.currentSearchByFlightNumber);
      }
    );
  }

  searchByFlightNumber($event: string) {
    this.currentSearchByFlightNumber = $event;
    this.flights = this.allFlights.filter(f => f.flightNumber.includes($event));
  }
}
