import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRouteFromTo } from '../route-from-to.model';
import { RouteFromToService } from '../service/route-from-to.service';

@Component({
  standalone: true,
  templateUrl: './route-from-to-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RouteFromToDeleteDialogComponent {
  routeFromTo?: IRouteFromTo;

  protected routeFromToService = inject(RouteFromToService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.routeFromToService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
