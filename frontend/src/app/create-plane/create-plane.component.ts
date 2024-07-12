import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AbstractControl, FormBuilder, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {ToastService} from "../service/impl/toast.service";
import {PlaneService} from "../service/impl/plane.service";
import {Plane} from "../data/plane";

@Component({
  selector: 'app-create-plane',
  templateUrl: './create-plane.component.html',
  styleUrl: './create-plane.component.css'
})
export class CreatePlaneComponent {

  @Input()
  show!: boolean;

  @Output()
  close = new EventEmitter<void>();
  createPlaneForm = this.formBuilder.group({
    registration: ['',
      {
        validators: [Validators.required,
          Validators.minLength(6),
          Validators.maxLength(6),
          this.registrationFormat()],
        updateOn: 'blur'
      }],
    type: ['',
      {
        validators: [Validators.required,
          Validators.maxLength(35)],
        updateOn: 'blur'
      }],
    pictureLink: ['',
      {
        validators: [Validators.required,
          Validators.maxLength(200),
          this.urlValidator()],
        updateOn: 'blur'
      }]
  });

  constructor(private formBuilder: FormBuilder,
              private toastService: ToastService,
              private planeService: PlaneService) {
  }

  get registration() {
    return this.createPlaneForm.controls['registration'];
  }

  get type() {
    return this.createPlaneForm.controls['type'];
  }

  get pictureLink() {
    return this.createPlaneForm.controls['pictureLink'];
  }

  closeModal() {
    this.close.emit();
  }

  registrationFormat(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const valid = /^[A-Z0-9-]+$/.test(control.value);
      return valid ? null : {registrationFormat: true};
    };
  }

  urlValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const urlPattern = /^(https?:\/\/)?([^\s$.?#].[^\s]*)$/i;
      const valid = urlPattern.test(control.value);
      return valid ? null : {invalidUrl: true};
    };
  }

  submitCreatePlane() {
    if (this.createPlaneForm.valid) {
      const newPlane: Plane = {
        registration: this.createPlaneForm.value.registration!,
        type: this.createPlaneForm.value.type!,
        pictureLink: this.createPlaneForm.value.pictureLink!,
      }
      this.createPlaneForm.reset();
      this.planeService.create(newPlane).subscribe(
        p => {
          if (p) {
            this.closeModal();
            this.toastService.showToast("Plane created successfully", "success");
          } else {
            this.toastService.showToast("An error occurred, please try again", "error");
          }
        }
      );
    } else {
      this.toastService.showToast("Check your plane and try again", "error");
    }

  }

}
