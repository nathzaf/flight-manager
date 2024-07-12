import {Injectable} from '@angular/core';
import {catchError, Observable} from "rxjs";
import {Passenger} from "../../data/passenger";
import CrudService from "../crud.service";

@Injectable()
export class PassengerService extends CrudService<Passenger, Passenger, Passenger> {

  private url = `${this.baseUrl}/v1/passengers`

  override getEndpointUrl(): string {
    return this.url;
  }


  getPilots(): Observable<Passenger[]> {
    return this.http.get<Passenger[]>(this.url + "/pilots")
      .pipe(catchError(this.handleError<Passenger[]>('GET')));
  }
}
