import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IRental } from '../rental.model';

@Component({
  standalone: true,
  selector: 'jhi-rental-detail',
  templateUrl: './rental-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RentalDetailComponent {
  rental = input<IRental | null>(null);

  previousState(): void {
    window.history.back();
  }
}
