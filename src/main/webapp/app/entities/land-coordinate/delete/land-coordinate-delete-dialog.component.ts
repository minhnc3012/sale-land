import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILandCoordinate } from '../land-coordinate.model';
import { LandCoordinateService } from '../service/land-coordinate.service';

@Component({
  templateUrl: './land-coordinate-delete-dialog.component.html',
})
export class LandCoordinateDeleteDialogComponent {
  landCoordinate?: ILandCoordinate;

  constructor(protected landCoordinateService: LandCoordinateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.landCoordinateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
