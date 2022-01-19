import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LandTagComponent } from './list/land-tag.component';
import { LandTagDetailComponent } from './detail/land-tag-detail.component';
import { LandTagUpdateComponent } from './update/land-tag-update.component';
import { LandTagDeleteDialogComponent } from './delete/land-tag-delete-dialog.component';
import { LandTagRoutingModule } from './route/land-tag-routing.module';

@NgModule({
  imports: [SharedModule, LandTagRoutingModule],
  declarations: [LandTagComponent, LandTagDetailComponent, LandTagUpdateComponent, LandTagDeleteDialogComponent],
  entryComponents: [LandTagDeleteDialogComponent],
})
export class LandTagModule {}
