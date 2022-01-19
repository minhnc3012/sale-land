import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILand, Land } from '../land.model';
import { LandService } from '../service/land.service';
import { LandStatus } from 'app/entities/enumerations/land-status.model';
import { LandType } from 'app/entities/enumerations/land-type.model';
import { UnitPriceType } from 'app/entities/enumerations/unit-price-type.model';
import { PriceType } from 'app/entities/enumerations/price-type.model';
import { FeeType } from 'app/entities/enumerations/fee-type.model';

@Component({
  selector: 'jhi-land-update',
  templateUrl: './land-update.component.html',
})
export class LandUpdateComponent implements OnInit {
  isSaving = false;
  landStatusValues = Object.keys(LandStatus);
  landTypeValues = Object.keys(LandType);
  unitPriceTypeValues = Object.keys(UnitPriceType);
  priceTypeValues = Object.keys(PriceType);
  feeTypeValues = Object.keys(FeeType);

  editForm = this.fb.group({
    id: [],
    title: [],
    address: [],
    status: [],
    type: [],
    price: [],
    init: [],
    priceType: [],
    feeType: [],
    description: [],
    width: [],
    height: [],
    area: [],
    latitude: [],
    longitude: [],
  });

  constructor(protected landService: LandService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ land }) => {
      this.updateForm(land);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const land = this.createFromForm();
    if (land.id !== undefined) {
      this.subscribeToSaveResponse(this.landService.update(land));
    } else {
      this.subscribeToSaveResponse(this.landService.create(land));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILand>>): void {
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

  protected updateForm(land: ILand): void {
    this.editForm.patchValue({
      id: land.id,
      title: land.title,
      address: land.address,
      status: land.status,
      type: land.type,
      price: land.price,
      init: land.init,
      priceType: land.priceType,
      feeType: land.feeType,
      description: land.description,
      width: land.width,
      height: land.height,
      area: land.area,
      latitude: land.latitude,
      longitude: land.longitude,
    });
  }

  protected createFromForm(): ILand {
    return {
      ...new Land(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      address: this.editForm.get(['address'])!.value,
      status: this.editForm.get(['status'])!.value,
      type: this.editForm.get(['type'])!.value,
      price: this.editForm.get(['price'])!.value,
      init: this.editForm.get(['init'])!.value,
      priceType: this.editForm.get(['priceType'])!.value,
      feeType: this.editForm.get(['feeType'])!.value,
      description: this.editForm.get(['description'])!.value,
      width: this.editForm.get(['width'])!.value,
      height: this.editForm.get(['height'])!.value,
      area: this.editForm.get(['area'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
    };
  }
}
