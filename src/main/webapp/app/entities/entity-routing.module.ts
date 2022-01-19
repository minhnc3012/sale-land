import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'land',
        data: { pageTitle: 'salelandApp.land.home.title' },
        loadChildren: () => import('./land/land.module').then(m => m.LandModule),
      },
      {
        path: 'land-tag',
        data: { pageTitle: 'salelandApp.landTag.home.title' },
        loadChildren: () => import('./land-tag/land-tag.module').then(m => m.LandTagModule),
      },
      {
        path: 'land-photo',
        data: { pageTitle: 'salelandApp.landPhoto.home.title' },
        loadChildren: () => import('./land-photo/land-photo.module').then(m => m.LandPhotoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
