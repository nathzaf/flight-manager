import {Component, Input, OnInit} from '@angular/core';
import {Feedback} from "../data/feedback";
import {FlightService} from "../service/impl/flight.service";
import {ToastService} from "../service/impl/toast.service";

@Component({
  selector: 'app-feedbacks',
  templateUrl: './feedbacks.component.html',
  styleUrl: './feedbacks.component.css'
})
export class FeedbacksComponent implements OnInit {

  @Input()
  flightNumber!: string;

  feedbacks: Feedback[] = [];

  showCreateFeedbackModal: boolean = false;

  isEligibleForNewFeedback: boolean = false;

  meanRating: number = 0;


  constructor(private flightService: FlightService,
              private toastService: ToastService) {
  }

  ngOnInit(): void {
    this.loadFeedbacks();
  }

  calculateAverageRating(feedbacks: Feedback[]): number {
    if (feedbacks.length === 0) {
      return 0; // ou une autre valeur par défaut
    }
    const totalRating = feedbacks.reduce((sum, feedback) => sum + feedback.rating, 0);
    const mean = totalRating / feedbacks.length;
    if (mean % 1 < 0.5) {
      return Math.floor(mean);
    } else {
      return Math.ceil(mean);
    }
  }

  onCreateFeedbackModalClosed() {
    this.showCreateFeedbackModal = false;
    this.loadFeedbacks();
  }

  setIsEligible($event: boolean) {
    this.isEligibleForNewFeedback = $event;
  }

  tryCreateNewFeedbackNotEligible() {
    this.toastService.showToast("All passengers on this flight have already given a feedback", "warning");
  }

  private loadFeedbacks() {
    this.flightService.getFeedbacksOnFlight(this.flightNumber).subscribe(
      feedbacks => {
        this.feedbacks = feedbacks;
        this.meanRating = this.calculateAverageRating(feedbacks);
      }
    );
  }
}
