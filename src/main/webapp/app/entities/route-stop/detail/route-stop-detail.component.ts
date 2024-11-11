import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IRouteStop } from '../route-stop.model';

@Component({
  standalone: true,
  selector: 'jhi-route-stop-detail',
  templateUrl: './route-stop-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RouteStopDetailComponent {
  routeStop = input<IRouteStop | null>(null);

  previousState(): void {
    window.history.back();
  }
}
