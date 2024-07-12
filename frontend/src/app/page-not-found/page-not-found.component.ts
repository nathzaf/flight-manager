import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.css'
})
export class PageNotFoundComponent implements OnInit {

  constructor(private router: Router) {
  }

  async ngOnInit(): Promise<void> {
    await new Promise(f => setTimeout(f, 2000));
    await this.router.navigate(['/']);
  }

}
