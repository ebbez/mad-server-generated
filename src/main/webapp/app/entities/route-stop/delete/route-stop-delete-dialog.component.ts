import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRouteStop } from '../route-stop.model';
import { RouteStopService } from '../service/route-stop.service';

@Component({
  standalone: true,
  templateUrl: './route-stop-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RouteStopDeleteDialogComponent {
  routeStop?: IRouteStop;

  protected routeStopService = inject(RouteStopService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.routeStopService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
