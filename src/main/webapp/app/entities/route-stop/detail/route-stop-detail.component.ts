import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IRouteStop } from '../route-stop.model';

@Component({
  selector: 'jhi-route-stop-detail',
  templateUrl: './route-stop-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class RouteStopDetailComponent {
  routeStop = input<IRouteStop | null>(null);

  previousState(): void {
    window.history.back();
  }
}
