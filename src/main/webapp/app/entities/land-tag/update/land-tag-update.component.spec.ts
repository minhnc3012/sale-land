import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LandTagService } from '../service/land-tag.service';
import { ILandTag, LandTag } from '../land-tag.model';
import { ILand } from 'app/entities/land/land.model';
import { LandService } from 'app/entities/land/service/land.service';

import { LandTagUpdateComponent } from './land-tag-update.component';

describe('LandTag Management Update Component', () => {
  let comp: LandTagUpdateComponent;
  let fixture: ComponentFixture<LandTagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let landTagService: LandTagService;
  let landService: LandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LandTagUpdateComponent],
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
      .overrideTemplate(LandTagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LandTagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    landTagService = TestBed.inject(LandTagService);
    landService = TestBed.inject(LandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Land query and add missing value', () => {
      const landTag: ILandTag = { id: 456 };
      const land: ILand = { id: 23458 };
      landTag.land = land;

      const landCollection: ILand[] = [{ id: 26283 }];
      jest.spyOn(landService, 'query').mockReturnValue(of(new HttpResponse({ body: landCollection })));
      const additionalLands = [land];
      const expectedCollection: ILand[] = [...additionalLands, ...landCollection];
      jest.spyOn(landService, 'addLandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ landTag });
      comp.ngOnInit();

      expect(landService.query).toHaveBeenCalled();
      expect(landService.addLandToCollectionIfMissing).toHaveBeenCalledWith(landCollection, ...additionalLands);
      expect(comp.landsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const landTag: ILandTag = { id: 456 };
      const land: ILand = { id: 22514 };
      landTag.land = land;

      activatedRoute.data = of({ landTag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(landTag));
      expect(comp.landsSharedCollection).toContain(land);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandTag>>();
      const landTag = { id: 123 };
      jest.spyOn(landTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: landTag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(landTagService.update).toHaveBeenCalledWith(landTag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandTag>>();
      const landTag = new LandTag();
      jest.spyOn(landTagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: landTag }));
      saveSubject.complete();

      // THEN
      expect(landTagService.create).toHaveBeenCalledWith(landTag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LandTag>>();
      const landTag = { id: 123 };
      jest.spyOn(landTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ landTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(landTagService.update).toHaveBeenCalledWith(landTag);
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
