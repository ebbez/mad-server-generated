import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICarReview } from '../car-review.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../car-review.test-samples';

import { CarReviewService } from './car-review.service';

const requireRestSample: ICarReview = {
  ...sampleWithRequiredData,
};

describe('CarReview Service', () => {
  let service: CarReviewService;
  let httpMock: HttpTestingController;
  let expectedResult: ICarReview | ICarReview[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CarReviewService);
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

    it('should create a CarReview', () => {
      const carReview = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(carReview).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CarReview', () => {
      const carReview = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(carReview).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CarReview', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CarReview', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CarReview', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCarReviewToCollectionIfMissing', () => {
      it('should add a CarReview to an empty array', () => {
        const carReview: ICarReview = sampleWithRequiredData;
        expectedResult = service.addCarReviewToCollectionIfMissing([], carReview);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carReview);
      });

      it('should not add a CarReview to an array that contains it', () => {
        const carReview: ICarReview = sampleWithRequiredData;
        const carReviewCollection: ICarReview[] = [
          {
            ...carReview,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCarReviewToCollectionIfMissing(carReviewCollection, carReview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CarReview to an array that doesn't contain it", () => {
        const carReview: ICarReview = sampleWithRequiredData;
        const carReviewCollection: ICarReview[] = [sampleWithPartialData];
        expectedResult = service.addCarReviewToCollectionIfMissing(carReviewCollection, carReview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carReview);
      });

      it('should add only unique CarReview to an array', () => {
        const carReviewArray: ICarReview[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const carReviewCollection: ICarReview[] = [sampleWithRequiredData];
        expectedResult = service.addCarReviewToCollectionIfMissing(carReviewCollection, ...carReviewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const carReview: ICarReview = sampleWithRequiredData;
        const carReview2: ICarReview = sampleWithPartialData;
        expectedResult = service.addCarReviewToCollectionIfMissing([], carReview, carReview2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carReview);
        expect(expectedResult).toContain(carReview2);
      });

      it('should accept null and undefined values', () => {
        const carReview: ICarReview = sampleWithRequiredData;
        expectedResult = service.addCarReviewToCollectionIfMissing([], null, carReview, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carReview);
      });

      it('should return initial array if no CarReview is added', () => {
        const carReviewCollection: ICarReview[] = [sampleWithRequiredData];
        expectedResult = service.addCarReviewToCollectionIfMissing(carReviewCollection, undefined, null);
        expect(expectedResult).toEqual(carReviewCollection);
      });
    });

    describe('compareCarReview', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCarReview(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28227 };
        const entity2 = null;

        const compareResult1 = service.compareCarReview(entity1, entity2);
        const compareResult2 = service.compareCarReview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28227 };
        const entity2 = { id: 3042 };

        const compareResult1 = service.compareCarReview(entity1, entity2);
        const compareResult2 = service.compareCarReview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28227 };
        const entity2 = { id: 28227 };

        const compareResult1 = service.compareCarReview(entity1, entity2);
        const compareResult2 = service.compareCarReview(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
