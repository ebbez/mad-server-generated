import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInspectionPhoto } from '../inspection-photo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../inspection-photo.test-samples';

import { InspectionPhotoService } from './inspection-photo.service';

const requireRestSample: IInspectionPhoto = {
  ...sampleWithRequiredData,
};

describe('InspectionPhoto Service', () => {
  let service: InspectionPhotoService;
  let httpMock: HttpTestingController;
  let expectedResult: IInspectionPhoto | IInspectionPhoto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InspectionPhotoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a InspectionPhoto', () => {
      const inspectionPhoto = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inspectionPhoto).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InspectionPhoto', () => {
      const inspectionPhoto = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inspectionPhoto).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InspectionPhoto', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InspectionPhoto', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InspectionPhoto', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInspectionPhotoToCollectionIfMissing', () => {
      it('should add a InspectionPhoto to an empty array', () => {
        const inspectionPhoto: IInspectionPhoto = sampleWithRequiredData;
        expectedResult = service.addInspectionPhotoToCollectionIfMissing([], inspectionPhoto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspectionPhoto);
      });

      it('should not add a InspectionPhoto to an array that contains it', () => {
        const inspectionPhoto: IInspectionPhoto = sampleWithRequiredData;
        const inspectionPhotoCollection: IInspectionPhoto[] = [
          {
            ...inspectionPhoto,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInspectionPhotoToCollectionIfMissing(inspectionPhotoCollection, inspectionPhoto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InspectionPhoto to an array that doesn't contain it", () => {
        const inspectionPhoto: IInspectionPhoto = sampleWithRequiredData;
        const inspectionPhotoCollection: IInspectionPhoto[] = [sampleWithPartialData];
        expectedResult = service.addInspectionPhotoToCollectionIfMissing(inspectionPhotoCollection, inspectionPhoto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspectionPhoto);
      });

      it('should add only unique InspectionPhoto to an array', () => {
        const inspectionPhotoArray: IInspectionPhoto[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inspectionPhotoCollection: IInspectionPhoto[] = [sampleWithRequiredData];
        expectedResult = service.addInspectionPhotoToCollectionIfMissing(inspectionPhotoCollection, ...inspectionPhotoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inspectionPhoto: IInspectionPhoto = sampleWithRequiredData;
        const inspectionPhoto2: IInspectionPhoto = sampleWithPartialData;
        expectedResult = service.addInspectionPhotoToCollectionIfMissing([], inspectionPhoto, inspectionPhoto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspectionPhoto);
        expect(expectedResult).toContain(inspectionPhoto2);
      });

      it('should accept null and undefined values', () => {
        const inspectionPhoto: IInspectionPhoto = sampleWithRequiredData;
        expectedResult = service.addInspectionPhotoToCollectionIfMissing([], null, inspectionPhoto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspectionPhoto);
      });

      it('should return initial array if no InspectionPhoto is added', () => {
        const inspectionPhotoCollection: IInspectionPhoto[] = [sampleWithRequiredData];
        expectedResult = service.addInspectionPhotoToCollectionIfMissing(inspectionPhotoCollection, undefined, null);
        expect(expectedResult).toEqual(inspectionPhotoCollection);
      });
    });

    describe('compareInspectionPhoto', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInspectionPhoto(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInspectionPhoto(entity1, entity2);
        const compareResult2 = service.compareInspectionPhoto(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInspectionPhoto(entity1, entity2);
        const compareResult2 = service.compareInspectionPhoto(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInspectionPhoto(entity1, entity2);
        const compareResult2 = service.compareInspectionPhoto(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
