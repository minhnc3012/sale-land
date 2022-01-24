import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LandPhotoDetailComponent } from './land-photo-detail.component';

describe('LandPhoto Management Detail Component', () => {
  let comp: LandPhotoDetailComponent;
  let fixture: ComponentFixture<LandPhotoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LandPhotoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ landPhoto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LandPhotoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LandPhotoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load landPhoto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.landPhoto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
