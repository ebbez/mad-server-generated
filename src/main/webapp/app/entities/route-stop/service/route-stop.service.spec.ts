import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRouteStop } from '../route-stop.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../route-stop.test-samples';

import { RouteStopService } from './route-stop.service';

const requireRestSample: IRouteStop = {
  ...sampleWithRequiredData,
};

describe('RouteStop Service', () => {
  let service: RouteStopService;
  let httpMock: HttpTestingController;
  let expectedResult: IRouteStop | IRouteStop[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RouteStopService);
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

    it('should create a RouteStop', () => {
      const routeStop = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(routeStop).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouteStop', () => {
      const routeStop = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(routeStop).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RouteStop', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RouteStop', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RouteStop', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRouteStopToCollectionIfMissing', () => {
      it('should add a RouteStop to an empty array', () => {
        const routeStop: IRouteStop = sampleWithRequiredData;
        expectedResult = service.addRouteStopToCollectionIfMissing([], routeStop);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(routeStop);
      });

      it('should not add a RouteStop to an array that contains it', () => {
        const routeStop: IRouteStop = sampleWithRequiredData;
        const routeStopCollection: IRouteStop[] = [
          {
            ...routeStop,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRouteStopToCollectionIfMissing(routeStopCollection, routeStop);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouteStop to an array that doesn't contain it", () => {
        const routeStop: IRouteStop = sampleWithRequiredData;
        const routeStopCollection: IRouteStop[] = [sampleWithPartialData];
        expectedResult = service.addRouteStopToCollectionIfMissing(routeStopCollection, routeStop);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(routeStop);
      });

      it('should add only unique RouteStop to an array', () => {
        const routeStopArray: IRouteStop[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const routeStopCollection: IRouteStop[] = [sampleWithRequiredData];
        expectedResult = service.addRouteStopToCollectionIfMissing(routeStopCollection, ...routeStopArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const routeStop: IRouteStop = sampleWithRequiredData;
        const routeStop2: IRouteStop = sampleWithPartialData;
        expectedResult = service.addRouteStopToCollectionIfMissing([], routeStop, routeStop2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(routeStop);
        expect(expectedResult).toContain(routeStop2);
      });

      it('should accept null and undefined values', () => {
        const routeStop: IRouteStop = sampleWithRequiredData;
        expectedResult = service.addRouteStopToCollectionIfMissing([], null, routeStop, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(routeStop);
      });

      it('should return initial array if no RouteStop is added', () => {
        const routeStopCollection: IRouteStop[] = [sampleWithRequiredData];
        expectedResult = service.addRouteStopToCollectionIfMissing(routeStopCollection, undefined, null);
        expect(expectedResult).toEqual(routeStopCollection);
      });
    });

    describe('compareRouteStop', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRouteStop(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRouteStop(entity1, entity2);
        const compareResult2 = service.compareRouteStop(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRouteStop(entity1, entity2);
        const compareResult2 = service.compareRouteStop(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRouteStop(entity1, entity2);
        const compareResult2 = service.compareRouteStop(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
