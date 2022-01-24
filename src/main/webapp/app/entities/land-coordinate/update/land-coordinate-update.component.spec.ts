import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LandCoordinateService } from '../service/land-coordinate.service';
import { ILandCoordinate, LandCoordinate } from '../land-coordinate.model';
import { ILand } from 'app/entities/land/land.model';
import { LandService } from 'app/entities/land/service/land.service';

import { LandCoordinateUpdateComponent } from './land-coordinate-update.component';

describe('LandCoordinate Management Update Component', () => {
  let comp: LandCoordinateUpdateComponent;
  let fixture: ComponentFixture<LandCoordinateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let landCoordinateService: LandCoordinateService;
  let landService: LandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LandCoordinateUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LandCoordinateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LandCoordinateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    landCoordinateService = TestBed.inject(LandCoordinateService);
    landService = TestBed.inject(LandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Land query and add missing value', () => {
      const landCoordinate: ILandCoordinate = { id: 456 };
      const land: ILand = { id: 53310 };
      landCoordinate.land = land;

      const landCollection: ILand[] = [{ id: 93202 }];
      jest.spyOn(landService, 'query').mockReturnValue(of(new HttpResponse({ body: landCollection })));
      const additionalLands = [land];
      const expectedCollection: ILand[] = [...additionalLands, ...landCollection];
      jest.spyOn(landService, 'addLandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ landCoordinate });
      comp.ngOnInit();

      expect(landService.query).toHaveBeenCalled();
      expect(landService.addLandToCollectionIfMissing).toHaveBeenCalledWith(landCollection, ...additionalLands);
      expect(comp.landsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const landCoordinate: ILandCoordinate = { id: 456 };
      const land: ILand = { id: 91414 };
      landCoordinate.land = land;

      activatedRoute.data = of({ landCoordinate });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(landCoordinate));
      expect(comp.landsSharedCollection).toContain(land);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandCoordinate>>();
      const landCoordinate = { id: 123 };
      jest.spyOn(landCoordinateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landCoordinate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: landCoordinate }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(landCoordinateService.update).toHaveBeenCalledWith(landCoordinate);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandCoordinate>>();
      const landCoordinate = new LandCoordinate();
      jest.spyOn(landCoordinateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landCoordinate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: landCoordinate }));
      saveSubject.complete();

      // THEN
      expect(landCoordinateService.create).toHaveBeenCalledWith(landCoordinate);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandCoordinate>>();
      const landCoordinate = { id: 123 };
      jest.spyOn(landCoordinateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landCoordinate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(landCoordinateService.update).toHaveBeenCalledWith(landCoordinate);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLandById', () => {
      it('Should return tracked Land primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLandById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
