import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../service/impl/category.service";
import {FlightService} from "../service/impl/flight.service";
import {ToastService} from "../service/impl/toast.service";
import {PassengerService} from "../service/impl/passenger.service";
import {AirfieldService} from "../service/impl/airfield.service";
import {Category} from "../data/category";
import {Airfield} from "../data/airfield";
import {Passenger} from "../data/passenger";
import {Plane} from "../data/plane";
import {AbstractControl, FormBuilder, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {PlaneService} from "../service/impl/plane.service";
import {CreateFlightRequest} from "../data/flight";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-flight',
  templateUrl: './create-flight.component.html',
  styleUrl: './create-flight.component.css'
})
export class CreateFlightComponent implements OnInit {

  categories: Category[] = [];

  airfields: Airfield[] = [];

  pilots: Passenger[] = [];

  planes: Plane[] = [];

  currentDate: string = '';
  createFlightForm = this.formBuilder.group({
    categoryId: ['',
      {
        validators: [Validators.required],
        updateOn: 'blur'
      }],
    date: [new Date(),
      {
        validators: [Validators.required,
          this.futureDateValidator()],
        updateOn: 'blur'
      }],
    departureIcao: ['',
      {
        validators: [Validators.required],
        updateOn: 'blur'
      }],
    arrivalIcao: ['',
      {
        validators: [Validators.required],
        updateOn: 'blur'
      }],
    planeRegistration: ['',
      {
        validators: [Validators.required],
        updateOn: 'blur'
      }],
    pilotUsername: ['',
      {
        validators: [Validators.required],
        updateOn: 'blur'
      }]
  });

  constructor(private categoryService: CategoryService,
              private flightService: FlightService,
              private passengerService: PassengerService,
              private airfieldService: AirfieldService,
              private planeService: PlaneService,
              private toastService: ToastService,
              private formBuilder: FormBuilder,
              private router: Router) {
  }

  get categoryId() {
    return this.createFlightForm.controls['categoryId'];
  }

  get date() {
    return this.createFlightForm.controls['date'];
  }

  get departureIcao() {
    return this.createFlightForm.controls['departureIcao'];
  }

  get arrivalIcao() {
    return this.createFlightForm.controls['arrivalIcao'];
  }

  get planeRegistration() {
    return this.createFlightForm.controls['planeRegistration'];
  }

  get pilotUsername() {
    return this.createFlightForm.controls['pilotUsername'];
  }

  ngOnInit(): void {
    this.categoryService.getAll().subscribe(
      categories => this.categories = categories
    );
    this.airfieldService.getAll().subscribe(
      airfields => this.airfields = airfields
    );
    this.planeService.getAll().subscribe(
      planes => this.planes = planes
    );
    this.passengerService.getPilots().subscribe(
      pilots => this.pilots = pilots
    );
    const now = new Date();
    this.currentDate = now.toISOString().slice(0, 16);
  }

  futureDateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const inputDate = new Date(control.value);
      const now = new Date();
      return inputDate > now ? null : {futureDate: true};
    };
  }

  submitCreateFlight() {
    if (this.createFlightForm.valid) {
      const newFlight: CreateFlightRequest = {
        categoryId: this.createFlightForm.value.categoryId!,
        date: this.createFlightForm.value.date!,
        departureIcao: this.createFlightForm.value.departureIcao!,
        arrivalIcao: this.createFlightForm.value.arrivalIcao!,
        planeRegistration: this.createFlightForm.value.planeRegistration!,
        pilotUsername: this.createFlightForm.value.pilotUsername!
      };
      this.createFlightForm.reset();
      this.flightService.create(newFlight).subscribe(f => {
        if (f) {
          this.toastService.showToast("Flight created successfully!", 'success');
          this.router.navigate(['/']);
        } else {
          this.toastService.showToast("An error occurred, please try again", 'error');
        }
      });
    } else {
      this.toastService.showToast("Check your flight and try again", 'error');
    }
  }
}
