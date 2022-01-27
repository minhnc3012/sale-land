import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ButtonConfirm } from '../dialog.service';

@Component({
  selector: 'jhi-confirm-dialog',
  templateUrl: './alert-dialog.component.html',
})
export class AlertDialogComponent {
  @Input() title?: string;
  @Input() message?: string;
  @Input() btnOk?: ButtonConfirm;

  constructor(private activeModal: NgbActiveModal) {}

  public accept(): void {
    this.activeModal.close(true);
  }

  public dismiss(): void {
    this.activeModal.dismiss();
  }
}
