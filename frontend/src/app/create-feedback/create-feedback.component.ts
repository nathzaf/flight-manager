import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {Passenger} from "../data/passenger";
import {FlightService} from "../service/impl/flight.service";
import {ToastService} from "../service/impl/toast.service";
import {CreateFeedbackRequest} from "../data/feedback";
import {FeedbackService} from "../service/impl/feedback.service";

@Component({
  selector: 'app-create-feedback',
  templateUrl: './create-feedback.component.html',
  styleUrl: './create-feedback.component.css'
})
export class CreateFeedbackComponent implements OnInit {

  @Input()
  show!: boolean;

  @Input()
  flightNumber!: string;

  eligiblePassengers: Passenger[] = [];

  @Output()
  close = new EventEmitter<void>();

  @Output()
  isEligible = new EventEmitter<boolean>();
  createFeedbackForm = this.formBuilder.group({
    comment: ['',
      {
        validators: [Validators.required,
          Validators.maxLength(255)],
        updateOn: 'blur'
      }],
    rating: [0,
      {
        validators: [Validators.required,
          Validators.min(1),
          Validators.max(5)],
        updateOn: 'blur'
      }],
    passengerUsername: ['',
      {
        validators: [Validators.required,],
        updateOn: 'blur'
      }]
  });

  constructor(private formBuilder: FormBuilder,
              private toastService: ToastService,
              private flightService: FlightService,
              private feedbackService: FeedbackService) {
  }

  get comment() {
    return this.createFeedbackForm.controls['comment'];
  }

  get rating() {
    return this.createFeedbackForm.controls['rating'];
  }

  get passengerUsername() {
    return this.createFeedbackForm.controls['passengerUsername'];
  }

  closeModal() {
    this.close.emit();
  }

  ngOnInit(): void {
    this.loadEligiblePassengers();
  }

  updateRating(newRating: number) {
    this.createFeedbackForm.patchValue({rating: newRating});
  }

  submitCreateFeedback() {
    if (this.createFeedbackForm.valid) {
      this.loadEligiblePassengers();
      const newFeedback: CreateFeedbackRequest = {
        passengerUsername: this.createFeedbackForm.value.passengerUsername!,
        flightNumber: this.flightNumber,
        comment: this.createFeedbackForm.value.comment!,
        rating: this.createFeedbackForm.value.rating!
      };
      this.createFeedbackForm.reset();
      this.feedbackService.create(newFeedback).subscribe(f => {
        if (f) {
          this.loadEligiblePassengers();
          this.closeModal();
          this.toastService.showToast("Feedback created successfully", "success");
        } else {
          this.toastService.showToast("An error occurred, please try again", "error");
        }
      });
    } else {
      this.toastService.showToast("Check your feedback and try again", "error");
    }

  }

  private loadEligiblePassengers() {
    this.flightService.getFeedbacksOnFlight(this.flightNumber).subscribe(
      feedbacks => {
        this.flightService.getPassengerBookedOnFlight(this.flightNumber).subscribe(
          bPassengers => {
            const feedbackUsernames = feedbacks.map(f => f.passenger.username);
            this.eligiblePassengers = bPassengers.filter(bp => !feedbackUsernames.includes(bp.username));
            if (this.eligiblePassengers.length == 0) {
              this.isEligible.emit(false);
            } else {
              this.isEligible.emit(true);
            }
          }
        );
      }
    );
  }

}
