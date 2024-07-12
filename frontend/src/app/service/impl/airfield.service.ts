import {Injectable} from '@angular/core';
import {Airfield, UpdateAirfieldRequest} from "../../data/airfield";
import CrudService from "../crud.service";

@Injectable()
export class AirfieldService extends CrudService<Airfield, Airfield, UpdateAirfieldRequest> {

  private url = `${this.baseUrl}/v1/airfields`

  override getEndpointUrl(): string {
    return this.url;
  }


}
