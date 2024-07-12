import {Component, Input} from '@angular/core';
import {Feedback} from "../data/feedback";

@Component({
  selector: 'app-feedback-card',
  templateUrl: './feedback-card.component.html',
  styleUrl: './feedback-card.component.css'
})
export class FeedbackCardComponent {

  @Input()
  feedback!: Feedback;

}
