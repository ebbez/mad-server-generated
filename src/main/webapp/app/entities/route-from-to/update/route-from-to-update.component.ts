import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRouteFromTo } from '../route-from-to.model';
import { RouteFromToService } from '../service/route-from-to.service';
import { RouteFromToFormGroup, RouteFromToFormService } from './route-from-to-form.service';

@Component({
  standalone: true,
  selector: 'jhi-route-from-to-update',
  templateUrl: './route-from-to-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RouteFromToUpdateComponent implements OnInit {
  isSaving = false;
  routeFromTo: IRouteFromTo | null = null;

  employeesSharedCollection: IEmployee[] = [];

  protected routeFromToService = inject(RouteFromToService);
  protected routeFromToFormService = inject(RouteFromToFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RouteFromToFormGroup = this.routeFromToFormService.createRouteFromToFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ routeFromTo }) => {
      this.routeFromTo = routeFromTo;
      if (routeFromTo) {
        this.updateForm(routeFromTo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const routeFromTo = this.routeFromToFormService.getRouteFromTo(this.editForm);
    if (routeFromTo.id !== null) {
      this.subscribeToSaveResponse(this.routeFromToService.update(routeFromTo));
    } else {
      this.subscribeToSaveResponse(this.routeFromToService.create(routeFromTo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouteFromTo>>): void {
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

  protected updateForm(routeFromTo: IRouteFromTo): void {
    this.routeFromTo = routeFromTo;
    this.routeFromToFormService.resetForm(this.editForm, routeFromTo);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      routeFromTo.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.routeFromTo?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
