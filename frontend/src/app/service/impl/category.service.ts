import {Injectable} from '@angular/core';
import {Category} from "../../data/category";
import CrudService from "../crud.service";

@Injectable()
export class CategoryService extends CrudService<Category, Category, Category> {

  private url = `${this.baseUrl}/v1/categories`

  override getEndpointUrl(): string {
    return this.url;
  }

}
