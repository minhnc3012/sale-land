import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILandCoordinate } from '../land-coordinate.model';

@Component({
  selector: 'jhi-land-coordinate-detail',
  templateUrl: './land-coordinate-detail.component.html',
})
export class LandCoordinateDetailComponent implements OnInit {
  landCoordinate: ILandCoordinate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ landCoordinate }) => {
      this.landCoordinate = landCoordinate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
