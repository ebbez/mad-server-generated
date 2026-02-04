import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RouteFromToDetailComponent } from './route-from-to-detail.component';

describe('RouteFromTo Management Detail Component', () => {
  let comp: RouteFromToDetailComponent;
  let fixture: ComponentFixture<RouteFromToDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouteFromToDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./route-from-to-detail.component').then(m => m.RouteFromToDetailComponent),
              resolve: { routeFromTo: () => of({ id: 23404 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RouteFromToDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RouteFromToDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load routeFromTo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RouteFromToDetailComponent);

      // THEN
      expect(instance.routeFromTo()).toEqual(expect.objectContaining({ id: 23404 }));
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
