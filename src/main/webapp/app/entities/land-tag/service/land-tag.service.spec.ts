import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILandTag, LandTag } from '../land-tag.model';

import { LandTagService } from './land-tag.service';

describe('LandTag Service', () => {
  let service: LandTagService;
  let httpMock: HttpTestingController;
  let elemDefault: ILandTag;
  let expectedResult: ILandTag | ILandTag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LandTagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      tag: 'AAAAAAA',
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

    it('should create a LandTag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LandTag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LandTag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tag: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LandTag', () => {
      const patchObject = Object.assign({}, new LandTag());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LandTag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tag: 'BBBBBB',
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

    it('should delete a LandTag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLandTagToCollectionIfMissing', () => {
      it('should add a LandTag to an empty array', () => {
        const landTag: ILandTag = { id: 123 };
        expectedResult = service.addLandTagToCollectionIfMissing([], landTag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(landTag);
      });

      it('should not add a LandTag to an array that contains it', () => {
        const landTag: ILandTag = { id: 123 };
        const landTagCollection: ILandTag[] = [
          {
            ...landTag,
          },
          { id: 456 },
        ];
        expectedResult = service.addLandTagToCollectionIfMissing(landTagCollection, landTag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LandTag to an array that doesn't contain it", () => {
        const landTag: ILandTag = { id: 123 };
        const landTagCollection: ILandTag[] = [{ id: 456 }];
        expectedResult = service.addLandTagToCollectionIfMissing(landTagCollection, landTag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(landTag);
      });

      it('should add only unique LandTag to an array', () => {
        const landTagArray: ILandTag[] = [{ id: 123 }, { id: 456 }, { id: 22985 }];
        const landTagCollection: ILandTag[] = [{ id: 123 }];
        expectedResult = service.addLandTagToCollectionIfMissing(landTagCollection, ...landTagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const landTag: ILandTag = { id: 123 };
        const landTag2: ILandTag = { id: 456 };
        expectedResult = service.addLandTagToCollectionIfMissing([], landTag, landTag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(landTag);
        expect(expectedResult).toContain(landTag2);
      });

      it('should accept null and undefined values', () => {
        const landTag: ILandTag = { id: 123 };
        expectedResult = service.addLandTagToCollectionIfMissing([], null, landTag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(landTag);
      });

      it('should return initial array if no LandTag is added', () => {
        const landTagCollection: ILandTag[] = [{ id: 123 }];
        expectedResult = service.addLandTagToCollectionIfMissing(landTagCollection, undefined, null);
        expect(expectedResult).toEqual(landTagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
