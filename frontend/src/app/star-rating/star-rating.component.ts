import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.css'
})
export class StarRatingComponent implements OnChanges {
  @Input()
  enabled = true;

  @Input()
  rating!: number;

  @Output()
  ratingChange = new EventEmitter<number>();

  stars: boolean[] = Array(5).fill(false);

  ngOnChanges(changes: SimpleChanges): void {
    this.updateStars(this.rating)
  }

  updateStars(rating: number) {
    this.stars = this.stars.map((_, i) => i < rating);
  }

  handleStarClick(index: number) {
    this.rating = index + 1;
    this.updateStars(this.rating);
    this.ratingChange.emit(this.rating);
  }


}
