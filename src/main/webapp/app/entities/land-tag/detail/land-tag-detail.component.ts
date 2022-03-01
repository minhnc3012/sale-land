import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILandTag } from '../land-tag.model';

@Component({
  selector: 'jhi-land-tag-detail',
  templateUrl: './land-tag-detail.component.html',
})
export class LandTagDetailComponent implements OnInit {
  landTag: ILandTag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ landTag }) => {
      this.landTag = landTag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
