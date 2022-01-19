import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LandCoordinateComponent } from '../list/land-coordinate.component';
import { LandCoordinateDetailComponent } from '../detail/land-coordinate-detail.component';
import { LandCoordinateUpdateComponent } from '../update/land-coordinate-update.component';
import { LandCoordinateRoutingResolveService } from './land-coordinate-routing-resolve.service';

const landCoordinateRoute: Routes = [
  {
    path: '',
    component: LandCoordinateComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LandCoordinateDetailComponent,
    resolve: {
      landCoordinate: LandCoordinateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LandCoordinateUpdateComponent,
    resolve: {
      landCoordinate: LandCoordinateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LandCoordinateUpdateComponent,
    resolve: {
      landCoordinate: LandCoordinateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(landCoordinateRoute)],
  exports: [RouterModule],
})
export class LandCoordinateRoutingModule {}
