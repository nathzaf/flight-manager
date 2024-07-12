import {Injectable} from '@angular/core';
import {Booking, CreateBookingRequest, UpdateBookingRequest} from "../../data/booking";
import CrudService from "../crud.service";
import {catchError, Observable} from "rxjs";

@Injectable()
export class BookingService extends CrudService<Booking, CreateBookingRequest, UpdateBookingRequest> {

  private url = `${this.baseUrl}/v1/bookings`

  override getEndpointUrl(): string {
    return this.url;
  }

  bookFlight(createBookingRequest: CreateBookingRequest): Observable<Booking> {
    return this.http.post<Booking>(this.url, createBookingRequest)
      .pipe(catchError(this.handleError<Booking>('Get flights by criteria', undefined)));
  }
}
