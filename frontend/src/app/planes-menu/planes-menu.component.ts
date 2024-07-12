import {Component, OnInit} from '@angular/core';
import {Plane} from "../data/plane";
import {PlaneService} from "../service/impl/plane.service";

@Component({
  selector: 'app-planes-menu',
  templateUrl: './planes-menu.component.html',
  styleUrl: './planes-menu.component.css'
})
export class PlanesMenuComponent implements OnInit {

  planes: Plane[] = [];

  showCreatePlaneModal: boolean = false;

  constructor(private planeService: PlaneService) {
  }

  ngOnInit(): void {
    this.loadPlanes();
  }

  onCreatePlaneModalClose() {
    this.showCreatePlaneModal = false;
    this.loadPlanes();
  }

  private loadPlanes() {
    this.planeService.getAll().subscribe(
      planes => {
        this.planes = planes.reverse();
      }
    );
  }
}
