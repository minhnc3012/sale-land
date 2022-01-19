import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LandCoordinateComponent } from './list/land-coordinate.component';
import { LandCoordinateDetailComponent } from './detail/land-coordinate-detail.component';
import { LandCoordinateUpdateComponent } from './update/land-coordinate-update.component';
import { LandCoordinateDeleteDialogComponent } from './delete/land-coordinate-delete-dialog.component';
import { LandCoordinateRoutingModule } from './route/land-coordinate-routing.module';

@NgModule({
  imports: [SharedModule, LandCoordinateRoutingModule],
  declarations: [
    LandCoordinateComponent,
    LandCoordinateDetailComponent,
    LandCoordinateUpdateComponent,
    LandCoordinateDeleteDialogComponent,
  ],
  entryComponents: [LandCoordinateDeleteDialogComponent],
})
export class LandCoordinateModule {}
