import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILandPhoto, LandPhoto } from '../land-photo.model';
import { LandPhotoService } from '../service/land-photo.service';
import { ILand } from 'app/entities/land/land.model';
import { LandService } from 'app/entities/land/service/land.service';

@Component({
  selector: 'jhi-land-photo-update',
  templateUrl: './land-photo-update.component.html',
})
export class LandPhotoUpdateComponent implements OnInit {
  isSaving = false;

  landsSharedCollection: ILand[] = [];

  editForm = this.fb.group({
    id: [],
    imageUrl: [],
    land: [],
  });

  constructor(
    protected landPhotoService: LandPhotoService,
    protected landService: LandService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ landPhoto }) => {
      this.updateForm(landPhoto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const landPhoto = this.createFromForm();
    if (landPhoto.id !== undefined) {
      this.subscribeToSaveResponse(this.landPhotoService.update(landPhoto));
    } else {
      this.subscribeToSaveResponse(this.landPhotoService.create(landPhoto));
    }
  }

  trackLandById(index: number, item: ILand): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILandPhoto>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(landPhoto: ILandPhoto): void {
    this.editForm.patchValue({
      id: landPhoto.id,
      imageUrl: landPhoto.imageUrl,
      land: landPhoto.land,
    });

    this.landsSharedCollection = this.landService.addLandToCollectionIfMissing(this.landsSharedCollection, landPhoto.land);
  }

  protected loadRelationshipsOptions(): void {
    this.landService
      .query()
      .pipe(map((res: HttpResponse<ILand[]>) => res.body ?? []))
      .pipe(map((lands: ILand[]) => this.landService.addLandToCollectionIfMissing(lands, this.editForm.get('land')!.value)))
      .subscribe((lands: ILand[]) => (this.landsSharedCollection = lands));
  }

  protected createFromForm(): ILandPhoto {
    return {
      ...new LandPhoto(),
      id: this.editForm.get(['id'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      land: this.editForm.get(['land'])!.value,
    };
  }
}
