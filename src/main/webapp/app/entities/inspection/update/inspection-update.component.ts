import { Component, ElementRef, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRental } from 'app/entities/rental/rental.model';
import { RentalService } from 'app/entities/rental/service/rental.service';
import { InspectionService } from '../service/inspection.service';
import { IInspection } from '../inspection.model';
import { InspectionFormGroup, InspectionFormService } from './inspection-form.service';

@Component({
  standalone: true,
  selector: 'jhi-inspection-update',
  templateUrl: './inspection-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InspectionUpdateComponent implements OnInit {
  isSaving = false;
  inspection: IInspection | null = null;

  carsSharedCollection: ICar[] = [];
  employeesSharedCollection: IEmployee[] = [];
  rentalsSharedCollection: IRental[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected inspectionService = inject(InspectionService);
  protected inspectionFormService = inject(InspectionFormService);
  protected carService = inject(CarService);
  protected employeeService = inject(EmployeeService);
  protected rentalService = inject(RentalService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InspectionFormGroup = this.inspectionFormService.createInspectionFormGroup();

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareRental = (o1: IRental | null, o2: IRental | null): boolean => this.rentalService.compareRental(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspection }) => {
      this.inspection = inspection;
      if (inspection) {
        this.updateForm(inspection);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('autoMaatApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector(`#${idInput}`)) {
      this.elementRef.nativeElement.querySelector(`#${idInput}`).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspection = this.inspectionFormService.getInspection(this.editForm);
    if (inspection.id !== null) {
      this.subscribeToSaveResponse(this.inspectionService.update(inspection));
    } else {
      this.subscribeToSaveResponse(this.inspectionService.create(inspection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspection>>): void {
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

  protected updateForm(inspection: IInspection): void {
    this.inspection = inspection;
    this.inspectionFormService.resetForm(this.editForm, inspection);

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, inspection.car);
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      inspection.employee,
    );
    this.rentalsSharedCollection = this.rentalService.addRentalToCollectionIfMissing<IRental>(
      this.rentalsSharedCollection,
      inspection.rental,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.inspection?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.inspection?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.rentalService
      .query()
      .pipe(map((res: HttpResponse<IRental[]>) => res.body ?? []))
      .pipe(map((rentals: IRental[]) => this.rentalService.addRentalToCollectionIfMissing<IRental>(rentals, this.inspection?.rental)))
      .subscribe((rentals: IRental[]) => (this.rentalsSharedCollection = rentals));
  }
}
