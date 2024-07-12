import {Category} from "./category";
import {Airfield} from "./airfield";
import {Plane} from "./plane";
import {Passenger} from "./passenger";

export interface Flight {
  flightNumber: string;
  category: Category;
  date: Date;
  creationDate: Date;
  departure: Airfield;
  arrival: Airfield;
  plane: Plane;
  pilot: Passenger;
}

export interface CreateFlightRequest {
  categoryId: string;
  date: Date;
  departureIcao: string;
  arrivalIcao: string;
  planeRegistration: string;
  pilotUsername: string;
}

export type UpdateFlightRequest = CreateFlightRequest & {
  flightNumber: string;
}
