import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILandCoordinate, LandCoordinate } from '../land-coordinate.model';
import { LandCoordinateService } from '../service/land-coordinate.service';
import { ILand } from 'app/entities/land/land.model';
import { LandService } from 'app/entities/land/service/land.service';

@Component({
  selector: 'jhi-land-coordinate-update',
  templateUrl: './land-coordinate-update.component.html',
})
export class LandCoordinateUpdateComponent implements OnInit {
  isSaving = false;

  landsSharedCollection: ILand[] = [];

  editForm = this.fb.group({
    id: [],
    latitude: [],
    longitude: [],
    land: [],
  });

  constructor(
    protected landCoordinateService: LandCoordinateService,
    protected landService: LandService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ landCoordinate }) => {
      this.updateForm(landCoordinate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const landCoordinate = this.createFromForm();
    if (landCoordinate.id !== undefined) {
      this.subscribeToSaveResponse(this.landCoordinateService.update(landCoordinate));
    } else {
      this.subscribeToSaveResponse(this.landCoordinateService.create(landCoordinate));
    }
  }

  trackLandById(index: number, item: ILand): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILandCoordinate>>): void {
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

  protected updateForm(landCoordinate: ILandCoordinate): void {
    this.editForm.patchValue({
      id: landCoordinate.id,
      latitude: landCoordinate.latitude,
      longitude: landCoordinate.longitude,
      land: landCoordinate.land,
    });

    this.landsSharedCollection = this.landService.addLandToCollectionIfMissing(this.landsSharedCollection, landCoordinate.land);
  }

  protected loadRelationshipsOptions(): void {
    this.landService
      .query()
      .pipe(map((res: HttpResponse<ILand[]>) => res.body ?? []))
      .pipe(map((lands: ILand[]) => this.landService.addLandToCollectionIfMissing(lands, this.editForm.get('land')!.value)))
      .subscribe((lands: ILand[]) => (this.landsSharedCollection = lands));
  }

  protected createFromForm(): ILandCoordinate {
    return {
      ...new LandCoordinate(),
      id: this.editForm.get(['id'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      land: this.editForm.get(['land'])!.value,
    };
  }
}
