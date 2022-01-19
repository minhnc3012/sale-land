import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILandCoordinate, LandCoordinate } from '../land-coordinate.model';
import { LandCoordinateService } from '../service/land-coordinate.service';

@Injectable({ providedIn: 'root' })
export class LandCoordinateRoutingResolveService implements Resolve<ILandCoordinate> {
  constructor(protected service: LandCoordinateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILandCoordinate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((landCoordinate: HttpResponse<LandCoordinate>) => {
          if (landCoordinate.body) {
            return of(landCoordinate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LandCoordinate());
  }
}
