import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILandCoordinate, LandCoordinate } from '../land-coordinate.model';

import { LandCoordinateService } from './land-coordinate.service';

describe('LandCoordinate Service', () => {
  let service: LandCoordinateService;
  let httpMock: HttpTestingController;
  let elemDefault: ILandCoordinate;
  let expectedResult: ILandCoordinate | ILandCoordinate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LandCoordinateService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      latitude: 0,
      longitude: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a LandCoordinate', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LandCoordinate()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LandCoordinate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          latitude: 1,
          longitude: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LandCoordinate', () => {
      const patchObject = Object.assign(
        {
          latitude: 1,
        },
        new LandCoordinate()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LandCoordinate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          latitude: 1,
          longitude: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LandCoordinate', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLandCoordinateToCollectionIfMissing', () => {
      it('should add a LandCoordinate to an empty array', () => {
        const landCoordinate: ILandCoordinate = { id: 123 };
        expectedResult = service.addLandCoordinateToCollectionIfMissing([], landCoordinate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(landCoordinate);
      });

      it('should not add a LandCoordinate to an array that contains it', () => {
        const landCoordinate: ILandCoordinate = { id: 123 };
        const landCoordinateCollection: ILandCoordinate[] = [
          {
            ...landCoordinate,
          },
          { id: 456 },
        ];
        expectedResult = service.addLandCoordinateToCollectionIfMissing(landCoordinateCollection, landCoordinate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LandCoordinate to an array that doesn't contain it", () => {
        const landCoordinate: ILandCoordinate = { id: 123 };
        const landCoordinateCollection: ILandCoordinate[] = [{ id: 456 }];
        expectedResult = service.addLandCoordinateToCollectionIfMissing(landCoordinateCollection, landCoordinate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(landCoordinate);
      });

      it('should add only unique LandCoordinate to an array', () => {
        const landCoordinateArray: ILandCoordinate[] = [{ id: 123 }, { id: 456 }, { id: 38711 }];
        const landCoordinateCollection: ILandCoordinate[] = [{ id: 123 }];
        expectedResult = service.addLandCoordinateToCollectionIfMissing(landCoordinateCollection, ...landCoordinateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const landCoordinate: ILandCoordinate = { id: 123 };
        const landCoordinate2: ILandCoordinate = { id: 456 };
        expectedResult = service.addLandCoordinateToCollectionIfMissing([], landCoordinate, landCoordinate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(landCoordinate);
        expect(expectedResult).toContain(landCoordinate2);
      });

      it('should accept null and undefined values', () => {
        const landCoordinate: ILandCoordinate = { id: 123 };
        expectedResult = service.addLandCoordinateToCollectionIfMissing([], null, landCoordinate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(landCoordinate);
      });

      it('should return initial array if no LandCoordinate is added', () => {
        const landCoordinateCollection: ILandCoordinate[] = [{ id: 123 }];
        expectedResult = service.addLandCoordinateToCollectionIfMissing(landCoordinateCollection, undefined, null);
        expect(expectedResult).toEqual(landCoordinateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
