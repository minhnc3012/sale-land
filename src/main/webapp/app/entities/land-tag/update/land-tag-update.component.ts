import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILandTag, LandTag } from '../land-tag.model';
import { LandTagService } from '../service/land-tag.service';
import { ILand } from 'app/entities/land/land.model';
import { LandService } from 'app/entities/land/service/land.service';

@Component({
  selector: 'jhi-land-tag-update',
  templateUrl: './land-tag-update.component.html',
})
export class LandTagUpdateComponent implements OnInit {
  isSaving = false;

  landsSharedCollection: ILand[] = [];

  editForm = this.fb.group({
    id: [],
    tag: [],
    land: [],
  });

  constructor(
    protected landTagService: LandTagService,
    protected landService: LandService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ landTag }) => {
      this.updateForm(landTag);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const landTag = this.createFromForm();
    if (landTag.id !== undefined) {
      this.subscribeToSaveResponse(this.landTagService.update(landTag));
    } else {
      this.subscribeToSaveResponse(this.landTagService.create(landTag));
    }
  }

  trackLandById(index: number, item: ILand): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILandTag>>): void {
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

  protected updateForm(landTag: ILandTag): void {
    this.editForm.patchValue({
      id: landTag.id,
      tag: landTag.tag,
      land: landTag.land,
    });

    this.landsSharedCollection = this.landService.addLandToCollectionIfMissing(this.landsSharedCollection, landTag.land);
  }

  protected loadRelationshipsOptions(): void {
    this.landService
      .query()
      .pipe(map((res: HttpResponse<ILand[]>) => res.body ?? []))
      .pipe(map((lands: ILand[]) => this.landService.addLandToCollectionIfMissing(lands, this.editForm.get('land')!.value)))
      .subscribe((lands: ILand[]) => (this.landsSharedCollection = lands));
  }

  protected createFromForm(): ILandTag {
    return {
      ...new LandTag(),
      id: this.editForm.get(['id'])!.value,
      tag: this.editForm.get(['tag'])!.value,
      land: this.editForm.get(['land'])!.value,
    };
  }
}
