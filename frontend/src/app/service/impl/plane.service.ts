import {Injectable} from '@angular/core';
import {Plane} from "../../data/plane";
import CrudService from "../crud.service";

@Injectable()
export class PlaneService extends CrudService<Plane, Plane, Plane> {

  private url = `${this.baseUrl}/v1/planes`

  override getEndpointUrl(): string {
    return this.url;
  }

}
