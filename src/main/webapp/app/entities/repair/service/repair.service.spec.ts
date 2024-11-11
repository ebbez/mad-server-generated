import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRepair } from '../repair.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../repair.test-samples';

import { RepairService, RestRepair } from './repair.service';

const requireRestSample: RestRepair = {
  ...sampleWithRequiredData,
  dateCompleted: sampleWithRequiredData.dateCompleted?.format(DATE_FORMAT),
};

describe('Repair Service', () => {
  let service: RepairService;
  let httpMock: HttpTestingController;
  let expectedResult: IRepair | IRepair[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RepairService);
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

    it('should create a Repair', () => {
      const repair = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(repair).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Repair', () => {
      const repair = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(repair).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Repair', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Repair', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Repair', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRepairToCollectionIfMissing', () => {
      it('should add a Repair to an empty array', () => {
        const repair: IRepair = sampleWithRequiredData;
        expectedResult = service.addRepairToCollectionIfMissing([], repair);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(repair);
      });

      it('should not add a Repair to an array that contains it', () => {
        const repair: IRepair = sampleWithRequiredData;
        const repairCollection: IRepair[] = [
          {
            ...repair,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRepairToCollectionIfMissing(repairCollection, repair);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Repair to an array that doesn't contain it", () => {
        const repair: IRepair = sampleWithRequiredData;
        const repairCollection: IRepair[] = [sampleWithPartialData];
        expectedResult = service.addRepairToCollectionIfMissing(repairCollection, repair);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(repair);
      });

      it('should add only unique Repair to an array', () => {
        const repairArray: IRepair[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const repairCollection: IRepair[] = [sampleWithRequiredData];
        expectedResult = service.addRepairToCollectionIfMissing(repairCollection, ...repairArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const repair: IRepair = sampleWithRequiredData;
        const repair2: IRepair = sampleWithPartialData;
        expectedResult = service.addRepairToCollectionIfMissing([], repair, repair2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(repair);
        expect(expectedResult).toContain(repair2);
      });

      it('should accept null and undefined values', () => {
        const repair: IRepair = sampleWithRequiredData;
        expectedResult = service.addRepairToCollectionIfMissing([], null, repair, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(repair);
      });

      it('should return initial array if no Repair is added', () => {
        const repairCollection: IRepair[] = [sampleWithRequiredData];
        expectedResult = service.addRepairToCollectionIfMissing(repairCollection, undefined, null);
        expect(expectedResult).toEqual(repairCollection);
      });
    });

    describe('compareRepair', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRepair(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRepair(entity1, entity2);
        const compareResult2 = service.compareRepair(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRepair(entity1, entity2);
        const compareResult2 = service.compareRepair(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRepair(entity1, entity2);
        const compareResult2 = service.compareRepair(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
