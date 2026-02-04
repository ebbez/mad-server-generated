import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICarReview } from '../car-review.model';
import { CarReviewService } from '../service/car-review.service';

@Component({
  templateUrl: './car-review-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CarReviewDeleteDialogComponent {
  carReview?: ICarReview;

  protected carReviewService = inject(CarReviewService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.carReviewService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
