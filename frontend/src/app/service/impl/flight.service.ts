import {Injectable} from '@angular/core';
import {catchError, Observable} from "rxjs";
import {CreateFlightRequest, Flight, UpdateFlightRequest} from "../../data/flight";
import {Passenger} from "../../data/passenger";
import {Feedback} from "../../data/feedback";
import CrudService from "../crud.service";

@Injectable()
export class FlightService extends CrudService<Flight, CreateFlightRequest, UpdateFlightRequest> {

  private url = `${this.baseUrl}/v1/flights`

  override getEndpointUrl(): string {
    return this.url;
  }

  getFlightsByCriteria(criteria: string): Observable<Flight[]> {
    return this.http.get<Flight[]>(this.url + criteria)
      .pipe(catchError(this.handleError<Flight[]>('Get flights by criteria')));
  }

  getPassengerBookedOnFlight(id: string): Observable<Passenger[]> {
    return this.http.get<Passenger[]>(`${this.url}/${id}/passengers`)
      .pipe(catchError(this.handleError<Passenger[]>('Get passengers booked on flight')));
  }

  getFeedbacksOnFlight(id: string): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.url}/${id}/feedbacks`)
      .pipe(catchError(this.handleError<Feedback[]>('Get feedbacks on flight')));
  }

}
