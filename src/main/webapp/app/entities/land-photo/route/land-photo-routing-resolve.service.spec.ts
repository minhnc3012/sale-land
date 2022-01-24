import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILandPhoto, LandPhoto } from '../land-photo.model';
import { LandPhotoService } from '../service/land-photo.service';

import { LandPhotoRoutingResolveService } from './land-photo-routing-resolve.service';

describe('LandPhoto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LandPhotoRoutingResolveService;
  let service: LandPhotoService;
  let resultLandPhoto: ILandPhoto | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(LandPhotoRoutingResolveService);
    service = TestBed.inject(LandPhotoService);
    resultLandPhoto = undefined;
  });

  describe('resolve', () => {
    it('should return ILandPhoto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLandPhoto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLandPhoto).toEqual({ id: 123 });
    });

    it('should return new ILandPhoto if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLandPhoto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLandPhoto).toEqual(new LandPhoto());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LandPhoto })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLandPhoto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLandPhoto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
