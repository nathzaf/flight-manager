import {Passenger} from "./passenger";
import {Flight} from "./flight";

export interface Booking {
  id: string;
  passenger: Passenger;
  flight: Flight;
}

export interface CreateBookingRequest {
  passengerUsername: string;
  flightNumber: string;
}

export type UpdateBookingRequest = CreateBookingRequest & {
  id: string;
};


