import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LandTagDetailComponent } from './land-tag-detail.component';

describe('LandTag Management Detail Component', () => {
  let comp: LandTagDetailComponent;
  let fixture: ComponentFixture<LandTagDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LandTagDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ landTag: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LandTagDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LandTagDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load landTag on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.landTag).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
