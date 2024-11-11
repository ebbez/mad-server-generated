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
import { IInspection } from 'app/entities/inspection/inspection.model';
import { InspectionService } from 'app/entities/inspection/service/inspection.service';
import { InspectionPhotoService } from '../service/inspection-photo.service';
import { IInspectionPhoto } from '../inspection-photo.model';
import { InspectionPhotoFormGroup, InspectionPhotoFormService } from './inspection-photo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-inspection-photo-update',
  templateUrl: './inspection-photo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InspectionPhotoUpdateComponent implements OnInit {
  isSaving = false;
  inspectionPhoto: IInspectionPhoto | null = null;

  inspectionsSharedCollection: IInspection[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected inspectionPhotoService = inject(InspectionPhotoService);
  protected inspectionPhotoFormService = inject(InspectionPhotoFormService);
  protected inspectionService = inject(InspectionService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InspectionPhotoFormGroup = this.inspectionPhotoFormService.createInspectionPhotoFormGroup();

  compareInspection = (o1: IInspection | null, o2: IInspection | null): boolean => this.inspectionService.compareInspection(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspectionPhoto }) => {
      this.inspectionPhoto = inspectionPhoto;
      if (inspectionPhoto) {
        this.updateForm(inspectionPhoto);
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
    const inspectionPhoto = this.inspectionPhotoFormService.getInspectionPhoto(this.editForm);
    if (inspectionPhoto.id !== null) {
      this.subscribeToSaveResponse(this.inspectionPhotoService.update(inspectionPhoto));
    } else {
      this.subscribeToSaveResponse(this.inspectionPhotoService.create(inspectionPhoto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspectionPhoto>>): void {
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

  protected updateForm(inspectionPhoto: IInspectionPhoto): void {
    this.inspectionPhoto = inspectionPhoto;
    this.inspectionPhotoFormService.resetForm(this.editForm, inspectionPhoto);

    this.inspectionsSharedCollection = this.inspectionService.addInspectionToCollectionIfMissing<IInspection>(
      this.inspectionsSharedCollection,
      inspectionPhoto.inspection,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inspectionService
      .query()
      .pipe(map((res: HttpResponse<IInspection[]>) => res.body ?? []))
      .pipe(
        map((inspections: IInspection[]) =>
          this.inspectionService.addInspectionToCollectionIfMissing<IInspection>(inspections, this.inspectionPhoto?.inspection),
        ),
      )
      .subscribe((inspections: IInspection[]) => (this.inspectionsSharedCollection = inspections));
  }
}
