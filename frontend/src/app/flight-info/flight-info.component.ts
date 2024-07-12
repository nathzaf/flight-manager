import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FlightService} from "../service/impl/flight.service";
import {Flight} from "../data/flight";

@Component({
  selector: 'app-flight-info',
  templateUrl: './flight-info.component.html',
  styleUrl: './flight-info.component.css'
})
export class FlightInfoComponent implements OnInit {

  flight!: Flight;

  isPassed: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private flightService: FlightService) {
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id != null) {
      this.flightService.getById(id).subscribe(
        flight => {
          if (!flight) {
            this.router.navigate(['/404']);
          }
          this.flight = flight;
          this.isPassed = new Date(this.flight.date) <= new Date();
        }
      );
    }
  }

}
