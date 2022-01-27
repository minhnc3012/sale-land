import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { GoogleMapsModule } from '@angular/google-maps';
import { ResizeObserverModule } from '@ng-web-apis/resize-observer';
import { AngularSplitModule } from 'angular-split';

@NgModule({
  exports: [
    FormsModule,
    CommonModule,
    NgbModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    TranslateModule,
    GoogleMapsModule,
    ResizeObserverModule,
    AngularSplitModule,
  ],
})
export class SharedLibsModule {}
