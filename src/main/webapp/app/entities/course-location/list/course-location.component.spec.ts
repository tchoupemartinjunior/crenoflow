import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CourseLocationService } from '../service/course-location.service';

import { CourseLocationComponent } from './course-location.component';

describe('CourseLocation Management Component', () => {
  let comp: CourseLocationComponent;
  let fixture: ComponentFixture<CourseLocationComponent>;
  let service: CourseLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'course-location', component: CourseLocationComponent }]),
        HttpClientTestingModule,
        CourseLocationComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CourseLocationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseLocationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CourseLocationService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.courseLocations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to courseLocationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCourseLocationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCourseLocationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
