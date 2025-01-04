jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SchoolLevelService } from '../service/school-level.service';

import { SchoolLevelDeleteDialogComponent } from './school-level-delete-dialog.component';

describe('SchoolLevel Management Delete Component', () => {
  let comp: SchoolLevelDeleteDialogComponent;
  let fixture: ComponentFixture<SchoolLevelDeleteDialogComponent>;
  let service: SchoolLevelService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SchoolLevelDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SchoolLevelDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SchoolLevelDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SchoolLevelService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
