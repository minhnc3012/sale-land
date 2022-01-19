import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LandPhotoService } from '../service/land-photo.service';
import { ILandPhoto, LandPhoto } from '../land-photo.model';
import { ILand } from 'app/entities/land/land.model';
import { LandService } from 'app/entities/land/service/land.service';

import { LandPhotoUpdateComponent } from './land-photo-update.component';

describe('LandPhoto Management Update Component', () => {
  let comp: LandPhotoUpdateComponent;
  let fixture: ComponentFixture<LandPhotoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let landPhotoService: LandPhotoService;
  let landService: LandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LandPhotoUpdateComponent],
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
      .overrideTemplate(LandPhotoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LandPhotoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    landPhotoService = TestBed.inject(LandPhotoService);
    landService = TestBed.inject(LandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Land query and add missing value', () => {
      const landPhoto: ILandPhoto = { id: 456 };
      const land: ILand = { id: 59697 };
      landPhoto.land = land;

      const landCollection: ILand[] = [{ id: 52485 }];
      jest.spyOn(landService, 'query').mockReturnValue(of(new HttpResponse({ body: landCollection })));
      const additionalLands = [land];
      const expectedCollection: ILand[] = [...additionalLands, ...landCollection];
      jest.spyOn(landService, 'addLandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ landPhoto });
      comp.ngOnInit();

      expect(landService.query).toHaveBeenCalled();
      expect(landService.addLandToCollectionIfMissing).toHaveBeenCalledWith(landCollection, ...additionalLands);
      expect(comp.landsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const landPhoto: ILandPhoto = { id: 456 };
      const land: ILand = { id: 51807 };
      landPhoto.land = land;

      activatedRoute.data = of({ landPhoto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(landPhoto));
      expect(comp.landsSharedCollection).toContain(land);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandPhoto>>();
      const landPhoto = { id: 123 };
      jest.spyOn(landPhotoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landPhoto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: landPhoto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(landPhotoService.update).toHaveBeenCalledWith(landPhoto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandPhoto>>();
      const landPhoto = new LandPhoto();
      jest.spyOn(landPhotoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landPhoto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: landPhoto }));
      saveSubject.complete();

      // THEN
      expect(landPhotoService.create).toHaveBeenCalledWith(landPhoto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandPhoto>>();
      const landPhoto = { id: 123 };
      jest.spyOn(landPhotoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landPhoto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(landPhotoService.update).toHaveBeenCalledWith(landPhoto);
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
