import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILandTag, LandTag } from '../land-tag.model';
import { LandTagService } from '../service/land-tag.service';

@Injectable({ providedIn: 'root' })
export class LandTagRoutingResolveService implements Resolve<ILandTag> {
  constructor(protected service: LandTagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILandTag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((landTag: HttpResponse<LandTag>) => {
          if (landTag.body) {
            return of(landTag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LandTag());
  }
}
