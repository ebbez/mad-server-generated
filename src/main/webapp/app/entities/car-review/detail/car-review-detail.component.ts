import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICarReview } from '../car-review.model';

@Component({
  selector: 'jhi-car-review-detail',
  templateUrl: './car-review-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CarReviewDetailComponent {
  carReview = input<ICarReview | null>(null);

  previousState(): void {
    window.history.back();
  }
}
