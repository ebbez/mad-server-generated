import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RouteStopDetailComponent } from './route-stop-detail.component';

describe('RouteStop Management Detail Component', () => {
  let comp: RouteStopDetailComponent;
  let fixture: ComponentFixture<RouteStopDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouteStopDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./route-stop-detail.component').then(m => m.RouteStopDetailComponent),
              resolve: { routeStop: () => of({ id: 6529 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RouteStopDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RouteStopDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load routeStop on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RouteStopDetailComponent);

      // THEN
      expect(instance.routeStop()).toEqual(expect.objectContaining({ id: 6529 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
