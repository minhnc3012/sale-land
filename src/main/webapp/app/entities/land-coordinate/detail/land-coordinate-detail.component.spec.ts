import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LandCoordinateDetailComponent } from './land-coordinate-detail.component';

describe('LandCoordinate Management Detail Component', () => {
  let comp: LandCoordinateDetailComponent;
  let fixture: ComponentFixture<LandCoordinateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LandCoordinateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ landCoordinate: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LandCoordinateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LandCoordinateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load landCoordinate on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.landCoordinate).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
