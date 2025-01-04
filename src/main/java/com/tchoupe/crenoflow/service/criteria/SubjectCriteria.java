package com.tchoupe.crenoflow.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.tchoupe.crenoflow.domain.Subject} entity. This class is used
 * in {@link com.tchoupe.crenoflow.web.rest.SubjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subjects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter label;

    private LongFilter subjectCycleId;

    private Boolean distinct;

    public SubjectCriteria() {}

    public SubjectCriteria(SubjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.subjectCycleId = other.subjectCycleId == null ? null : other.subjectCycleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubjectCriteria copy() {
        return new SubjectCriteria(this);
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

    public StringFilter getLabel() {
        return label;
    }

    public StringFilter label() {
        if (label == null) {
            label = new StringFilter();
        }
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public LongFilter getSubjectCycleId() {
        return subjectCycleId;
    }

    public LongFilter subjectCycleId() {
        if (subjectCycleId == null) {
            subjectCycleId = new LongFilter();
        }
        return subjectCycleId;
    }

    public void setSubjectCycleId(LongFilter subjectCycleId) {
        this.subjectCycleId = subjectCycleId;
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
        final SubjectCriteria that = (SubjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label) &&
            Objects.equals(subjectCycleId, that.subjectCycleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, subjectCycleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (label != null ? "label=" + label + ", " : "") +
            (subjectCycleId != null ? "subjectCycleId=" + subjectCycleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
