import {Component, Input, OnInit} from '@angular/core';
import {Passenger} from "../data/passenger";
import {FlightService} from "../service/impl/flight.service";
import {PassengerService} from "../service/impl/passenger.service";
import {BookingService} from "../service/impl/booking.service";
import {ToastService} from "../service/impl/toast.service";

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css'
})
export class BookingComponent implements OnInit {

  @Input()
  flightNumber!: string;

  @Input()
  pilotUsername!: string;

  showCreateUserModal: boolean = false;

  bookedPassengers: Passenger[] = [];

  eligiblePassengers: Passenger[] = []

  toBookPassenger!: string;

  constructor(private flightService: FlightService,
              private passengerService: PassengerService,
              private bookingService: BookingService,
              private toastService: ToastService) {
  }

  ngOnInit(): void {
    this.loadPassengersList();
  }

  bookFlight() {
    if (!this.toBookPassenger) {
      this.toastService.showToast('Please select a passenger to book the flight', 'error');
      window.location.reload();
    }
    const createBookingRequest = {
      passengerUsername: this.toBookPassenger,
      flightNumber: this.flightNumber
    };
    this.bookingService.bookFlight(createBookingRequest).subscribe(
      res => {
        if (res) {
          this.loadPassengersList();
          this.toastService.showToast('Flight booked successfully by ' + this.toBookPassenger, 'success');
        } else {
          this.toastService.showToast('An error occurred, please try again', 'error');
          window.location.reload();
        }

      }
    );
  }

  onCreateUserModalClosed() {
    this.showCreateUserModal = false;
    this.loadPassengersList();
  }

  private loadPassengersList() {
    let allPassenger: Passenger[] = [];
    this.passengerService.getAll().subscribe(
      allPassengers => {
        allPassenger = allPassengers
        this.flightService.getPassengerBookedOnFlight(this.flightNumber).subscribe(
          bPassengers => {
            this.bookedPassengers = bPassengers;
            this.eligiblePassengers = allPassenger.filter(p =>
              !this.bookedPassengers.some(bp => bp.username === p.username) && p.username !== this.pilotUsername);
            this.toBookPassenger = this.eligiblePassengers[0].username;
          }
        );
      }
    );
  }
}
