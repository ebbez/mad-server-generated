import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IRouteFromTo } from 'app/entities/route-from-to/route-from-to.model';
import { RouteFromToService } from 'app/entities/route-from-to/service/route-from-to.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IRouteStop } from '../route-stop.model';
import { RouteStopService } from '../service/route-stop.service';
import { RouteStopFormService } from './route-stop-form.service';

import { RouteStopUpdateComponent } from './route-stop-update.component';

describe('RouteStop Management Update Component', () => {
  let comp: RouteStopUpdateComponent;
  let fixture: ComponentFixture<RouteStopUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let routeStopFormService: RouteStopFormService;
  let routeStopService: RouteStopService;
  let routeFromToService: RouteFromToService;
  let locationService: LocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouteStopUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RouteStopUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouteStopUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    routeStopFormService = TestBed.inject(RouteStopFormService);
    routeStopService = TestBed.inject(RouteStopService);
    routeFromToService = TestBed.inject(RouteFromToService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RouteFromTo query and add missing value', () => {
      const routeStop: IRouteStop = { id: 456 };
      const routeFromTo: IRouteFromTo = { id: 18127 };
      routeStop.routeFromTo = routeFromTo;

      const routeFromToCollection: IRouteFromTo[] = [{ id: 9823 }];
      jest.spyOn(routeFromToService, 'query').mockReturnValue(of(new HttpResponse({ body: routeFromToCollection })));
      const additionalRouteFromTos = [routeFromTo];
      const expectedCollection: IRouteFromTo[] = [...additionalRouteFromTos, ...routeFromToCollection];
      jest.spyOn(routeFromToService, 'addRouteFromToToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ routeStop });
      comp.ngOnInit();

      expect(routeFromToService.query).toHaveBeenCalled();
      expect(routeFromToService.addRouteFromToToCollectionIfMissing).toHaveBeenCalledWith(
        routeFromToCollection,
        ...additionalRouteFromTos.map(expect.objectContaining),
      );
      expect(comp.routeFromTosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Location query and add missing value', () => {
      const routeStop: IRouteStop = { id: 456 };
      const location: ILocation = { id: 14568 };
      routeStop.location = location;

      const locationCollection: ILocation[] = [{ id: 7756 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [location];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ routeStop });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(
        locationCollection,
        ...additionalLocations.map(expect.objectContaining),
      );
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const routeStop: IRouteStop = { id: 456 };
      const routeFromTo: IRouteFromTo = { id: 20001 };
      routeStop.routeFromTo = routeFromTo;
      const location: ILocation = { id: 15076 };
      routeStop.location = location;

      activatedRoute.data = of({ routeStop });
      comp.ngOnInit();

      expect(comp.routeFromTosSharedCollection).toContain(routeFromTo);
      expect(comp.locationsSharedCollection).toContain(location);
      expect(comp.routeStop).toEqual(routeStop);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRouteStop>>();
      const routeStop = { id: 123 };
      jest.spyOn(routeStopFormService, 'getRouteStop').mockReturnValue(routeStop);
      jest.spyOn(routeStopService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ routeStop });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: routeStop }));
      saveSubject.complete();

      // THEN
      expect(routeStopFormService.getRouteStop).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(routeStopService.update).toHaveBeenCalledWith(expect.objectContaining(routeStop));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRouteStop>>();
      const routeStop = { id: 123 };
      jest.spyOn(routeStopFormService, 'getRouteStop').mockReturnValue({ id: null });
      jest.spyOn(routeStopService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ routeStop: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: routeStop }));
      saveSubject.complete();

      // THEN
      expect(routeStopFormService.getRouteStop).toHaveBeenCalled();
      expect(routeStopService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRouteStop>>();
      const routeStop = { id: 123 };
      jest.spyOn(routeStopService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ routeStop });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(routeStopService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRouteFromTo', () => {
      it('Should forward to routeFromToService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(routeFromToService, 'compareRouteFromTo');
        comp.compareRouteFromTo(entity, entity2);
        expect(routeFromToService.compareRouteFromTo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
