import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ButtonConfirm } from '../dialog.service';

@Component({
  selector: 'jhi-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
})
export class ConfirmDialogComponent {
  @Input() title?: string;
  @Input() message?: string;
  @Input() btnOk?: ButtonConfirm;
  @Input() btnCancel?: ButtonConfirm;

  constructor(private activeModal: NgbActiveModal) {}

  public decline(): void {
    this.activeModal.close(false);
  }

  public accept(): void {
    this.activeModal.close(true);
  }

  public dismiss(): void {
    this.activeModal.dismiss();
  }
}
