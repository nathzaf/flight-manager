import {Component, Input} from '@angular/core';
import {Plane} from "../data/plane";

@Component({
  selector: 'app-plane-card',
  templateUrl: './plane-card.component.html',
  styleUrl: './plane-card.component.css'
})
export class PlaneCardComponent {

  @Input()
  plane!: Plane;

}
