import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILandPhoto, LandPhoto } from '../land-photo.model';

import { LandPhotoService } from './land-photo.service';

describe('LandPhoto Service', () => {
  let service: LandPhotoService;
  let httpMock: HttpTestingController;
  let elemDefault: ILandPhoto;
  let expectedResult: ILandPhoto | ILandPhoto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LandPhotoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      imageUrl: 'AAAAAAA',
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

    it('should create a LandPhoto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LandPhoto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LandPhoto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          imageUrl: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LandPhoto', () => {
      const patchObject = Object.assign({}, new LandPhoto());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LandPhoto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          imageUrl: 'BBBBBB',
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

    it('should delete a LandPhoto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLandPhotoToCollectionIfMissing', () => {
      it('should add a LandPhoto to an empty array', () => {
        const landPhoto: ILandPhoto = { id: 123 };
        expectedResult = service.addLandPhotoToCollectionIfMissing([], landPhoto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(landPhoto);
      });

      it('should not add a LandPhoto to an array that contains it', () => {
        const landPhoto: ILandPhoto = { id: 123 };
        const landPhotoCollection: ILandPhoto[] = [
          {
            ...landPhoto,
          },
          { id: 456 },
        ];
        expectedResult = service.addLandPhotoToCollectionIfMissing(landPhotoCollection, landPhoto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LandPhoto to an array that doesn't contain it", () => {
        const landPhoto: ILandPhoto = { id: 123 };
        const landPhotoCollection: ILandPhoto[] = [{ id: 456 }];
        expectedResult = service.addLandPhotoToCollectionIfMissing(landPhotoCollection, landPhoto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(landPhoto);
      });

      it('should add only unique LandPhoto to an array', () => {
        const landPhotoArray: ILandPhoto[] = [{ id: 123 }, { id: 456 }, { id: 68540 }];
        const landPhotoCollection: ILandPhoto[] = [{ id: 123 }];
        expectedResult = service.addLandPhotoToCollectionIfMissing(landPhotoCollection, ...landPhotoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const landPhoto: ILandPhoto = { id: 123 };
        const landPhoto2: ILandPhoto = { id: 456 };
        expectedResult = service.addLandPhotoToCollectionIfMissing([], landPhoto, landPhoto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(landPhoto);
        expect(expectedResult).toContain(landPhoto2);
      });

      it('should accept null and undefined values', () => {
        const landPhoto: ILandPhoto = { id: 123 };
        expectedResult = service.addLandPhotoToCollectionIfMissing([], null, landPhoto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(landPhoto);
      });

      it('should return initial array if no LandPhoto is added', () => {
        const landPhotoCollection: ILandPhoto[] = [{ id: 123 }];
        expectedResult = service.addLandPhotoToCollectionIfMissing(landPhotoCollection, undefined, null);
        expect(expectedResult).toEqual(landPhotoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
