import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRental } from '../rental.model';
import { RentalService } from '../service/rental.service';

@Component({
  standalone: true,
  templateUrl: './rental-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RentalDeleteDialogComponent {
  rental?: IRental;

  protected rentalService = inject(RentalService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rentalService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
