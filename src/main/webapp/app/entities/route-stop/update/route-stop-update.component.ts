import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRouteFromTo } from 'app/entities/route-from-to/route-from-to.model';
import { RouteFromToService } from 'app/entities/route-from-to/service/route-from-to.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { RouteStopService } from '../service/route-stop.service';
import { IRouteStop } from '../route-stop.model';
import { RouteStopFormGroup, RouteStopFormService } from './route-stop-form.service';

@Component({
  standalone: true,
  selector: 'jhi-route-stop-update',
  templateUrl: './route-stop-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RouteStopUpdateComponent implements OnInit {
  isSaving = false;
  routeStop: IRouteStop | null = null;

  routeFromTosSharedCollection: IRouteFromTo[] = [];
  locationsSharedCollection: ILocation[] = [];

  protected routeStopService = inject(RouteStopService);
  protected routeStopFormService = inject(RouteStopFormService);
  protected routeFromToService = inject(RouteFromToService);
  protected locationService = inject(LocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RouteStopFormGroup = this.routeStopFormService.createRouteStopFormGroup();

  compareRouteFromTo = (o1: IRouteFromTo | null, o2: IRouteFromTo | null): boolean => this.routeFromToService.compareRouteFromTo(o1, o2);

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ routeStop }) => {
      this.routeStop = routeStop;
      if (routeStop) {
        this.updateForm(routeStop);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const routeStop = this.routeStopFormService.getRouteStop(this.editForm);
    if (routeStop.id !== null) {
      this.subscribeToSaveResponse(this.routeStopService.update(routeStop));
    } else {
      this.subscribeToSaveResponse(this.routeStopService.create(routeStop));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouteStop>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(routeStop: IRouteStop): void {
    this.routeStop = routeStop;
    this.routeStopFormService.resetForm(this.editForm, routeStop);

    this.routeFromTosSharedCollection = this.routeFromToService.addRouteFromToToCollectionIfMissing<IRouteFromTo>(
      this.routeFromTosSharedCollection,
      routeStop.routeFromTo,
    );
    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsSharedCollection,
      routeStop.location,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.routeFromToService
      .query()
      .pipe(map((res: HttpResponse<IRouteFromTo[]>) => res.body ?? []))
      .pipe(
        map((routeFromTos: IRouteFromTo[]) =>
          this.routeFromToService.addRouteFromToToCollectionIfMissing<IRouteFromTo>(routeFromTos, this.routeStop?.routeFromTo),
        ),
      )
      .subscribe((routeFromTos: IRouteFromTo[]) => (this.routeFromTosSharedCollection = routeFromTos));

    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.routeStop?.location),
        ),
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }
}
