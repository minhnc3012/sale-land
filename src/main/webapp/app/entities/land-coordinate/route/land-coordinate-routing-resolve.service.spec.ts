import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILandCoordinate, LandCoordinate } from '../land-coordinate.model';
import { LandCoordinateService } from '../service/land-coordinate.service';

import { LandCoordinateRoutingResolveService } from './land-coordinate-routing-resolve.service';

describe('LandCoordinate routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LandCoordinateRoutingResolveService;
  let service: LandCoordinateService;
  let resultLandCoordinate: ILandCoordinate | undefined;

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
    routingResolveService = TestBed.inject(LandCoordinateRoutingResolveService);
    service = TestBed.inject(LandCoordinateService);
    resultLandCoordinate = undefined;
  });

  describe('resolve', () => {
    it('should return ILandCoordinate returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLandCoordinate = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLandCoordinate).toEqual({ id: 123 });
    });

    it('should return new ILandCoordinate if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLandCoordinate = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLandCoordinate).toEqual(new LandCoordinate());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LandCoordinate })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLandCoordinate = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLandCoordinate).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
