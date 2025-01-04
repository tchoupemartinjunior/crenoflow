jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AvailabilityService } from '../service/availability.service';

import { AvailabilityDeleteDialogComponent } from './availability-delete-dialog.component';

describe('Availability Management Delete Component', () => {
  let comp: AvailabilityDeleteDialogComponent;
  let fixture: ComponentFixture<AvailabilityDeleteDialogComponent>;
  let service: AvailabilityService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AvailabilityDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(AvailabilityDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AvailabilityDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AvailabilityService);
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
