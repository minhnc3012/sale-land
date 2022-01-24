import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILandPhoto } from '../land-photo.model';
import { LandPhotoService } from '../service/land-photo.service';

@Component({
  templateUrl: './land-photo-delete-dialog.component.html',
})
export class LandPhotoDeleteDialogComponent {
  landPhoto?: ILandPhoto;

  constructor(protected landPhotoService: LandPhotoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.landPhotoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
