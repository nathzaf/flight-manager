import {Injectable} from '@angular/core';
import CrudService from "../crud.service";
import {CreateFeedbackRequest, Feedback, UpdateFeedbackRequest} from "../../data/feedback";

@Injectable()
export class FeedbackService extends CrudService<Feedback, CreateFeedbackRequest, UpdateFeedbackRequest> {

  private url = `${this.baseUrl}/v1/feedbacks`

  override getEndpointUrl(): string {
    return this.url;
  }

}
