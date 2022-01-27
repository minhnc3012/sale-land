import { Injectable } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ConfirmDialogComponent } from './confirm/confirm-dialog.component';
import { AlertDialogComponent } from './alert/alert-dialog.component';

export class ButtonConfirm {
  constructor(public text: string | null = 'Button', public css?: string | null) {}
}
@Injectable()
export class DialogService {
  constructor(private modalService: NgbModal) {}

  public confirm(
    title?: string,
    message?: string,
    btnOk: ButtonConfirm | null = new ButtonConfirm('OK', 'btn btn-primary'),
    btnCancel: ButtonConfirm | null = new ButtonConfirm('Cancel', 'btn btn-secondary'),
    dialogSize: 'sm' | 'lg' = 'sm'
  ): Promise<boolean> {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { size: dialogSize, backdrop: 'static', windowClass: 'modal-confirm' });
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.btnOk = btnOk;
    modalRef.componentInstance.btnCancel = btnCancel;

    return modalRef.result as Promise<boolean>;
  }

  public alert(
    title?: string,
    message?: string,
    btnOk: ButtonConfirm | null = new ButtonConfirm('OK', 'btn btn-primary'),
    dialogSize: 'sm' | 'lg' = 'lg'
  ): Promise<boolean> {
    const modalRef = this.modalService.open(AlertDialogComponent, { size: dialogSize, backdrop: 'static', windowClass: 'modal-confirm' });
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.btnOk = btnOk;

    return modalRef.result as Promise<boolean>;
  }
}
