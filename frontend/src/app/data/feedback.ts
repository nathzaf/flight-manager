import {Passenger} from "./passenger";
import {Flight} from "./flight";

export interface Feedback {
  id: string;
  passenger: Passenger;
  flight: Flight;
  rating: number;
  comment: string;
}

export type CreateFeedbackRequest = Omit<Feedback, "id" | "passenger" | "flight"> & {
  passengerUsername: string;
  flightNumber: string;
}

export type UpdateFeedbackRequest = Omit<Feedback, "passenger" | "flight">;
