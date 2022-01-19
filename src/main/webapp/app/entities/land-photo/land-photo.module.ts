import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LandPhotoComponent } from './list/land-photo.component';
import { LandPhotoDetailComponent } from './detail/land-photo-detail.component';
import { LandPhotoUpdateComponent } from './update/land-photo-update.component';
import { LandPhotoDeleteDialogComponent } from './delete/land-photo-delete-dialog.component';
import { LandPhotoRoutingModule } from './route/land-photo-routing.module';

@NgModule({
  imports: [SharedModule, LandPhotoRoutingModule],
  declarations: [LandPhotoComponent, LandPhotoDetailComponent, LandPhotoUpdateComponent, LandPhotoDeleteDialogComponent],
  entryComponents: [LandPhotoDeleteDialogComponent],
})
export class LandPhotoModule {}
