import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInspection } from '../inspection.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../inspection.test-samples';

import { InspectionService, RestInspection } from './inspection.service';

const requireRestSample: RestInspection = {
  ...sampleWithRequiredData,
  completed: sampleWithRequiredData.completed?.toJSON(),
};

describe('Inspection Service', () => {
  let service: InspectionService;
  let httpMock: HttpTestingController;
  let expectedResult: IInspection | IInspection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InspectionService);
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

    it('should create a Inspection', () => {
      const inspection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inspection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inspection', () => {
      const inspection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inspection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inspection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inspection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Inspection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInspectionToCollectionIfMissing', () => {
      it('should add a Inspection to an empty array', () => {
        const inspection: IInspection = sampleWithRequiredData;
        expectedResult = service.addInspectionToCollectionIfMissing([], inspection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspection);
      });

      it('should not add a Inspection to an array that contains it', () => {
        const inspection: IInspection = sampleWithRequiredData;
        const inspectionCollection: IInspection[] = [
          {
            ...inspection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, inspection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inspection to an array that doesn't contain it", () => {
        const inspection: IInspection = sampleWithRequiredData;
        const inspectionCollection: IInspection[] = [sampleWithPartialData];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, inspection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspection);
      });

      it('should add only unique Inspection to an array', () => {
        const inspectionArray: IInspection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inspectionCollection: IInspection[] = [sampleWithRequiredData];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, ...inspectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inspection: IInspection = sampleWithRequiredData;
        const inspection2: IInspection = sampleWithPartialData;
        expectedResult = service.addInspectionToCollectionIfMissing([], inspection, inspection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspection);
        expect(expectedResult).toContain(inspection2);
      });

      it('should accept null and undefined values', () => {
        const inspection: IInspection = sampleWithRequiredData;
        expectedResult = service.addInspectionToCollectionIfMissing([], null, inspection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspection);
      });

      it('should return initial array if no Inspection is added', () => {
        const inspectionCollection: IInspection[] = [sampleWithRequiredData];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, undefined, null);
        expect(expectedResult).toEqual(inspectionCollection);
      });
    });

    describe('compareInspection', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInspection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInspection(entity1, entity2);
        const compareResult2 = service.compareInspection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInspection(entity1, entity2);
        const compareResult2 = service.compareInspection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInspection(entity1, entity2);
        const compareResult2 = service.compareInspection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
