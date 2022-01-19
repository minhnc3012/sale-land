import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILandTag } from '../land-tag.model';
import { LandTagService } from '../service/land-tag.service';

@Component({
  templateUrl: './land-tag-delete-dialog.component.html',
})
export class LandTagDeleteDialogComponent {
  landTag?: ILandTag;

  constructor(protected landTagService: LandTagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.landTagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
