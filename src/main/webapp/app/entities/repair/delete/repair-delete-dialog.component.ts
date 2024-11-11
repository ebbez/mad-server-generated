import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRepair } from '../repair.model';
import { RepairService } from '../service/repair.service';

@Component({
  standalone: true,
  templateUrl: './repair-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RepairDeleteDialogComponent {
  repair?: IRepair;

  protected repairService = inject(RepairService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.repairService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
