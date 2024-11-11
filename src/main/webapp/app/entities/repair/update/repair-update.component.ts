import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IInspection } from 'app/entities/inspection/inspection.model';
import { InspectionService } from 'app/entities/inspection/service/inspection.service';
import { RepairStatus } from 'app/entities/enumerations/repair-status.model';
import { RepairService } from '../service/repair.service';
import { IRepair } from '../repair.model';
import { RepairFormGroup, RepairFormService } from './repair-form.service';

@Component({
  standalone: true,
  selector: 'jhi-repair-update',
  templateUrl: './repair-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RepairUpdateComponent implements OnInit {
  isSaving = false;
  repair: IRepair | null = null;
  repairStatusValues = Object.keys(RepairStatus);

  carsSharedCollection: ICar[] = [];
  employeesSharedCollection: IEmployee[] = [];
  inspectionsSharedCollection: IInspection[] = [];

  protected repairService = inject(RepairService);
  protected repairFormService = inject(RepairFormService);
  protected carService = inject(CarService);
  protected employeeService = inject(EmployeeService);
  protected inspectionService = inject(InspectionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RepairFormGroup = this.repairFormService.createRepairFormGroup();

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareInspection = (o1: IInspection | null, o2: IInspection | null): boolean => this.inspectionService.compareInspection(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ repair }) => {
      this.repair = repair;
      if (repair) {
        this.updateForm(repair);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const repair = this.repairFormService.getRepair(this.editForm);
    if (repair.id !== null) {
      this.subscribeToSaveResponse(this.repairService.update(repair));
    } else {
      this.subscribeToSaveResponse(this.repairService.create(repair));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRepair>>): void {
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

  protected updateForm(repair: IRepair): void {
    this.repair = repair;
    this.repairFormService.resetForm(this.editForm, repair);

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, repair.car);
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      repair.employee,
    );
    this.inspectionsSharedCollection = this.inspectionService.addInspectionToCollectionIfMissing<IInspection>(
      this.inspectionsSharedCollection,
      repair.inspection,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.repair?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.repair?.employee)),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.inspectionService
      .query()
      .pipe(map((res: HttpResponse<IInspection[]>) => res.body ?? []))
      .pipe(
        map((inspections: IInspection[]) =>
          this.inspectionService.addInspectionToCollectionIfMissing<IInspection>(inspections, this.repair?.inspection),
        ),
      )
      .subscribe((inspections: IInspection[]) => (this.inspectionsSharedCollection = inspections));
  }
}
