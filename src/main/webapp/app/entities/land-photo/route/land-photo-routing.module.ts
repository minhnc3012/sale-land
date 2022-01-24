import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LandPhotoComponent } from '../list/land-photo.component';
import { LandPhotoDetailComponent } from '../detail/land-photo-detail.component';
import { LandPhotoUpdateComponent } from '../update/land-photo-update.component';
import { LandPhotoRoutingResolveService } from './land-photo-routing-resolve.service';

const landPhotoRoute: Routes = [
  {
    path: '',
    component: LandPhotoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LandPhotoDetailComponent,
    resolve: {
      landPhoto: LandPhotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LandPhotoUpdateComponent,
    resolve: {
      landPhoto: LandPhotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LandPhotoUpdateComponent,
    resolve: {
      landPhoto: LandPhotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(landPhotoRoute)],
  exports: [RouterModule],
})
export class LandPhotoRoutingModule {}
