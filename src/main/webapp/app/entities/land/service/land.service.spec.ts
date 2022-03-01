import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { LandStatus } from 'app/entities/enumerations/land-status.model';
import { LandType } from 'app/entities/enumerations/land-type.model';
import { UnitPriceType } from 'app/entities/enumerations/unit-price-type.model';
import { PriceType } from 'app/entities/enumerations/price-type.model';
import { FeeType } from 'app/entities/enumerations/fee-type.model';
import { ILand, Land } from '../land.model';

import { LandService } from './land.service';

describe('Land Service', () => {
  let service: LandService;
  let httpMock: HttpTestingController;
  let elemDefault: ILand;
  let expectedResult: ILand | ILand[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LandService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      title: 'AAAAAAA',
      address: 'AAAAAAA',
      status: LandStatus.A,
      type: LandType.A,
      price: 0,
      init: UnitPriceType.A,
      priceType: PriceType.A,
      feeType: FeeType.A,
      description: 'AAAAAAA',
      width: 0,
      height: 0,
      area: 0,
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

    it('should create a Land', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Land()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Land', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          address: 'BBBBBB',
          status: 'BBBBBB',
          type: 'BBBBBB',
          price: 1,
          init: 'BBBBBB',
          priceType: 'BBBBBB',
          feeType: 'BBBBBB',
          description: 'BBBBBB',
          width: 1,
          height: 1,
          area: 1,
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

    it('should partial update a Land', () => {
      const patchObject = Object.assign(
        {
          title: 'BBBBBB',
          type: 'BBBBBB',
          price: 1,
          priceType: 'BBBBBB',
          description: 'BBBBBB',
          height: 1,
          area: 1,
        },
        new Land()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Land', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          address: 'BBBBBB',
          status: 'BBBBBB',
          type: 'BBBBBB',
          price: 1,
          init: 'BBBBBB',
          priceType: 'BBBBBB',
          feeType: 'BBBBBB',
          description: 'BBBBBB',
          width: 1,
          height: 1,
          area: 1,
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

    it('should delete a Land', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLandToCollectionIfMissing', () => {
      it('should add a Land to an empty array', () => {
        const land: ILand = { id: 123 };
        expectedResult = service.addLandToCollectionIfMissing([], land);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(land);
      });

      it('should not add a Land to an array that contains it', () => {
        const land: ILand = { id: 123 };
        const landCollection: ILand[] = [
          {
            ...land,
          },
          { id: 456 },
        ];
        expectedResult = service.addLandToCollectionIfMissing(landCollection, land);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Land to an array that doesn't contain it", () => {
        const land: ILand = { id: 123 };
        const landCollection: ILand[] = [{ id: 456 }];
        expectedResult = service.addLandToCollectionIfMissing(landCollection, land);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(land);
      });

      it('should add only unique Land to an array', () => {
        const landArray: ILand[] = [{ id: 123 }, { id: 456 }, { id: 84528 }];
        const landCollection: ILand[] = [{ id: 123 }];
        expectedResult = service.addLandToCollectionIfMissing(landCollection, ...landArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const land: ILand = { id: 123 };
        const land2: ILand = { id: 456 };
        expectedResult = service.addLandToCollectionIfMissing([], land, land2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(land);
        expect(expectedResult).toContain(land2);
      });

      it('should accept null and undefined values', () => {
        const land: ILand = { id: 123 };
        expectedResult = service.addLandToCollectionIfMissing([], null, land, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(land);
      });

      it('should return initial array if no Land is added', () => {
        const landCollection: ILand[] = [{ id: 123 }];
        expectedResult = service.addLandToCollectionIfMissing(landCollection, undefined, null);
        expect(expectedResult).toEqual(landCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
