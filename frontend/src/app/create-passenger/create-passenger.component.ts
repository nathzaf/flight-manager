import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, ValidatorFn, Validators} from "@angular/forms";
import {Passenger} from "../data/passenger";
import {PassengerService} from "../service/impl/passenger.service";
import {ToastService} from "../service/impl/toast.service";

@Component({
  selector: 'app-create-passenger',
  templateUrl: './create-passenger.component.html',
  styleUrl: './create-passenger.component.css'
})
export class CreatePassengerComponent implements OnInit {

  @Input()
  show!: boolean;

  @Output()
  close = new EventEmitter<void>();
  passengers: Passenger[] = [];
  createPassengerForm = this.formBuilder.group({
    name: ['',
      {
        validators: [Validators.required,
          Validators.maxLength(100)],
        updateOn: 'blur'
      }],
    username: ['',
      {
        validators: [Validators.required,
          Validators.maxLength(100),
          this.isUsernameAvailable()],
        updateOn: 'blur'
      }],
    isPilot: [false]
  });

  constructor(private formBuilder: FormBuilder,
              private passengerService: PassengerService,
              private toastService: ToastService) {
  }

  get name() {
    return this.createPassengerForm.controls['name'];
  }

  get username() {
    return this.createPassengerForm.controls['username'];
  }

  get isPilot() {
    return this.createPassengerForm.controls['isPilot'];
  }

  closeModal() {
    this.close.emit();
  }

  ngOnInit(): void {
    this.loadPassengers();
  }

  isUsernameAvailable(): ValidatorFn {
    return (control) => {
      const username = control.value;

      if (!username) {
        return null;
      }

      const isTaken = this.passengers.some(passenger => passenger.username === username);

      return isTaken ? {usernameTaken: true} : null;
    };

  }

  submitCreatePassenger(): void {
    if (this.createPassengerForm.valid) {
      const newPassenger: Passenger = {
        name: this.createPassengerForm.value.name!,
        username: this.createPassengerForm.value.username!,
        isPilot: this.createPassengerForm.value.isPilot!
      };
      this.createPassengerForm.reset();
      this.passengerService.create(newPassenger).subscribe(p => {
        if (p) {
          this.loadPassengers()
          this.closeModal();
          this.toastService.showToast("Passenger created successfully", "success");
        } else {
          this.toastService.showToast("An error occurred, please try again", "error");
        }
      });
    } else {
      this.toastService.showToast("Check your passenger and try again", "error");
    }
  }

  private loadPassengers() {
    this.passengerService.getAll().subscribe(passengers => {
      this.passengers = passengers;
    });
  }

}
