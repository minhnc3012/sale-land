import { NgModule } from '@angular/core';
import { DialogService } from './dialog.service';
import { ConfirmDialogComponent } from './confirm/confirm-dialog.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AlertDialogComponent } from './alert/alert-dialog.component';

@NgModule({
  declarations: [ConfirmDialogComponent, AlertDialogComponent],
  imports: [NgbModule],
  exports: [ConfirmDialogComponent, AlertDialogComponent],
  providers: [DialogService],
  entryComponents: [ConfirmDialogComponent, AlertDialogComponent],
})
export class DialogModule {}
