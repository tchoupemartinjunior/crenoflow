package com.tchoupe.crenoflow.service.criteria;

import com.tchoupe.crenoflow.domain.enumeration.BookingStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.tchoupe.crenoflow.domain.Booking} entity. This class is used
 * in {@link com.tchoupe.crenoflow.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BookingStatus
     */
    public static class BookingStatusFilter extends Filter<BookingStatus> {

        public BookingStatusFilter() {}

        public BookingStatusFilter(BookingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookingStatusFilter copy() {
            return new BookingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BookingStatusFilter status;

    private InstantFilter bookingDate;

    private InstantFilter modificationDate;

    private LongFilter courseId;

    private LongFilter parentId;

    private LongFilter studentId;

    private Boolean distinct;

    public BookingCriteria() {}

    public BookingCriteria(BookingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.bookingDate = other.bookingDate == null ? null : other.bookingDate.copy();
        this.modificationDate = other.modificationDate == null ? null : other.modificationDate.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BookingStatusFilter getStatus() {
        return status;
    }

    public BookingStatusFilter status() {
        if (status == null) {
            status = new BookingStatusFilter();
        }
        return status;
    }

    public void setStatus(BookingStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getBookingDate() {
        return bookingDate;
    }

    public InstantFilter bookingDate() {
        if (bookingDate == null) {
            bookingDate = new InstantFilter();
        }
        return bookingDate;
    }

    public void setBookingDate(InstantFilter bookingDate) {
        this.bookingDate = bookingDate;
    }

    public InstantFilter getModificationDate() {
        return modificationDate;
    }

    public InstantFilter modificationDate() {
        if (modificationDate == null) {
            modificationDate = new InstantFilter();
        }
        return modificationDate;
    }

    public void setModificationDate(InstantFilter modificationDate) {
        this.modificationDate = modificationDate;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public LongFilter courseId() {
        if (courseId == null) {
            courseId = new LongFilter();
        }
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public LongFilter studentId() {
        if (studentId == null) {
            studentId = new LongFilter();
        }
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookingCriteria that = (BookingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(bookingDate, that.bookingDate) &&
            Objects.equals(modificationDate, that.modificationDate) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, bookingDate, modificationDate, courseId, parentId, studentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (bookingDate != null ? "bookingDate=" + bookingDate + ", " : "") +
            (modificationDate != null ? "modificationDate=" + modificationDate + ", " : "") +
            (courseId != null ? "courseId=" + courseId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
