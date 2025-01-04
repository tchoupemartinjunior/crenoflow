package com.tchoupe.crenoflow.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.tchoupe.crenoflow.domain.Course} entity. This class is used
 * in {@link com.tchoupe.crenoflow.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter teachersComment;

    private LongFilter subjectId;

    private LongFilter cycleId;

    private LongFilter locationId;

    private LongFilter teacherId;

    private LongFilter availabilityId;

    private Boolean distinct;

    public CourseCriteria() {}

    public CourseCriteria(CourseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.teachersComment = other.teachersComment == null ? null : other.teachersComment.copy();
        this.subjectId = other.subjectId == null ? null : other.subjectId.copy();
        this.cycleId = other.cycleId == null ? null : other.cycleId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.teacherId = other.teacherId == null ? null : other.teacherId.copy();
        this.availabilityId = other.availabilityId == null ? null : other.availabilityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
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

    public StringFilter getTeachersComment() {
        return teachersComment;
    }

    public StringFilter teachersComment() {
        if (teachersComment == null) {
            teachersComment = new StringFilter();
        }
        return teachersComment;
    }

    public void setTeachersComment(StringFilter teachersComment) {
        this.teachersComment = teachersComment;
    }

    public LongFilter getSubjectId() {
        return subjectId;
    }

    public LongFilter subjectId() {
        if (subjectId == null) {
            subjectId = new LongFilter();
        }
        return subjectId;
    }

    public void setSubjectId(LongFilter subjectId) {
        this.subjectId = subjectId;
    }

    public LongFilter getCycleId() {
        return cycleId;
    }

    public LongFilter cycleId() {
        if (cycleId == null) {
            cycleId = new LongFilter();
        }
        return cycleId;
    }

    public void setCycleId(LongFilter cycleId) {
        this.cycleId = cycleId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getTeacherId() {
        return teacherId;
    }

    public LongFilter teacherId() {
        if (teacherId == null) {
            teacherId = new LongFilter();
        }
        return teacherId;
    }

    public void setTeacherId(LongFilter teacherId) {
        this.teacherId = teacherId;
    }

    public LongFilter getAvailabilityId() {
        return availabilityId;
    }

    public LongFilter availabilityId() {
        if (availabilityId == null) {
            availabilityId = new LongFilter();
        }
        return availabilityId;
    }

    public void setAvailabilityId(LongFilter availabilityId) {
        this.availabilityId = availabilityId;
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
        final CourseCriteria that = (CourseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(teachersComment, that.teachersComment) &&
            Objects.equals(subjectId, that.subjectId) &&
            Objects.equals(cycleId, that.cycleId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(teacherId, that.teacherId) &&
            Objects.equals(availabilityId, that.availabilityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teachersComment, subjectId, cycleId, locationId, teacherId, availabilityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (teachersComment != null ? "teachersComment=" + teachersComment + ", " : "") +
            (subjectId != null ? "subjectId=" + subjectId + ", " : "") +
            (cycleId != null ? "cycleId=" + cycleId + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (teacherId != null ? "teacherId=" + teacherId + ", " : "") +
            (availabilityId != null ? "availabilityId=" + availabilityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
