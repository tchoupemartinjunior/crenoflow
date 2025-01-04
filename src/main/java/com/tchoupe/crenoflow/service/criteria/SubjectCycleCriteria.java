package com.tchoupe.crenoflow.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.tchoupe.crenoflow.domain.SubjectCycle} entity. This class is used
 * in {@link com.tchoupe.crenoflow.web.rest.SubjectCycleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subject-cycles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubjectCycleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter teacherId;

    private LongFilter subjectId;

    private LongFilter cycleId;

    private Boolean distinct;

    public SubjectCycleCriteria() {}

    public SubjectCycleCriteria(SubjectCycleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.teacherId = other.teacherId == null ? null : other.teacherId.copy();
        this.subjectId = other.subjectId == null ? null : other.subjectId.copy();
        this.cycleId = other.cycleId == null ? null : other.cycleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubjectCycleCriteria copy() {
        return new SubjectCycleCriteria(this);
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
        final SubjectCycleCriteria that = (SubjectCycleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(teacherId, that.teacherId) &&
            Objects.equals(subjectId, that.subjectId) &&
            Objects.equals(cycleId, that.cycleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teacherId, subjectId, cycleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubjectCycleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (teacherId != null ? "teacherId=" + teacherId + ", " : "") +
            (subjectId != null ? "subjectId=" + subjectId + ", " : "") +
            (cycleId != null ? "cycleId=" + cycleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
