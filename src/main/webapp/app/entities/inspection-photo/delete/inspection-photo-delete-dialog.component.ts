import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInspectionPhoto } from '../inspection-photo.model';
import { InspectionPhotoService } from '../service/inspection-photo.service';

@Component({
  standalone: true,
  templateUrl: './inspection-photo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InspectionPhotoDeleteDialogComponent {
  inspectionPhoto?: IInspectionPhoto;

  protected inspectionPhotoService = inject(InspectionPhotoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inspectionPhotoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
