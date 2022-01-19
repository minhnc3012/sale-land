import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILandPhoto } from '../land-photo.model';

@Component({
  selector: 'jhi-land-photo-detail',
  templateUrl: './land-photo-detail.component.html',
})
export class LandPhotoDetailComponent implements OnInit {
  landPhoto: ILandPhoto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ landPhoto }) => {
      this.landPhoto = landPhoto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
