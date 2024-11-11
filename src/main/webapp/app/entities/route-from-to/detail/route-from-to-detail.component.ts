import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IRouteFromTo } from '../route-from-to.model';

@Component({
  standalone: true,
  selector: 'jhi-route-from-to-detail',
  templateUrl: './route-from-to-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RouteFromToDetailComponent {
  routeFromTo = input<IRouteFromTo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
