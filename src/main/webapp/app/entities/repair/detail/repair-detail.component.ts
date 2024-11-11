import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IRepair } from '../repair.model';

@Component({
  standalone: true,
  selector: 'jhi-repair-detail',
  templateUrl: './repair-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RepairDetailComponent {
  repair = input<IRepair | null>(null);

  previousState(): void {
    window.history.back();
  }
}
