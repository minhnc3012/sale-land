import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LandTagComponent } from '../list/land-tag.component';
import { LandTagDetailComponent } from '../detail/land-tag-detail.component';
import { LandTagUpdateComponent } from '../update/land-tag-update.component';
import { LandTagRoutingResolveService } from './land-tag-routing-resolve.service';

const landTagRoute: Routes = [
  {
    path: '',
    component: LandTagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LandTagDetailComponent,
    resolve: {
      landTag: LandTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LandTagUpdateComponent,
    resolve: {
      landTag: LandTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LandTagUpdateComponent,
    resolve: {
      landTag: LandTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(landTagRoute)],
  exports: [RouterModule],
})
export class LandTagRoutingModule {}
