import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILandPhoto, LandPhoto } from '../land-photo.model';
import { LandPhotoService } from '../service/land-photo.service';

@Injectable({ providedIn: 'root' })
export class LandPhotoRoutingResolveService implements Resolve<ILandPhoto> {
  constructor(protected service: LandPhotoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILandPhoto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((landPhoto: HttpResponse<LandPhoto>) => {
          if (landPhoto.body) {
            return of(landPhoto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LandPhoto());
  }
}
