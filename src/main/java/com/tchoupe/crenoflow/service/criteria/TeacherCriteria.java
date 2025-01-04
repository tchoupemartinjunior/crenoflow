package com.tchoupe.crenoflow.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.tchoupe.crenoflow.domain.Teacher} entity. This class is used
 * in {@link com.tchoupe.crenoflow.web.rest.TeacherResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /teachers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeacherCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter introduction;

    private LongFilter personId;

    private LongFilter educationLevelId;

    private LongFilter professionId;

    private LongFilter specialityId;

    private LongFilter subjectCycleId;

    private Boolean distinct;

    public TeacherCriteria() {}

    public TeacherCriteria(TeacherCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.introduction = other.introduction == null ? null : other.introduction.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.educationLevelId = other.educationLevelId == null ? null : other.educationLevelId.copy();
        this.professionId = other.professionId == null ? null : other.professionId.copy();
        this.specialityId = other.specialityId == null ? null : other.specialityId.copy();
        this.subjectCycleId = other.subjectCycleId == null ? null : other.subjectCycleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TeacherCriteria copy() {
        return new TeacherCriteria(this);
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

    public StringFilter getIntroduction() {
        return introduction;
    }

    public StringFilter introduction() {
        if (introduction == null) {
            introduction = new StringFilter();
        }
        return introduction;
    }

    public void setIntroduction(StringFilter introduction) {
        this.introduction = introduction;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public LongFilter personId() {
        if (personId == null) {
            personId = new LongFilter();
        }
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    public LongFilter getEducationLevelId() {
        return educationLevelId;
    }

    public LongFilter educationLevelId() {
        if (educationLevelId == null) {
            educationLevelId = new LongFilter();
        }
        return educationLevelId;
    }

    public void setEducationLevelId(LongFilter educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public LongFilter getProfessionId() {
        return professionId;
    }

    public LongFilter professionId() {
        if (professionId == null) {
            professionId = new LongFilter();
        }
        return professionId;
    }

    public void setProfessionId(LongFilter professionId) {
        this.professionId = professionId;
    }

    public LongFilter getSpecialityId() {
        return specialityId;
    }

    public LongFilter specialityId() {
        if (specialityId == null) {
            specialityId = new LongFilter();
        }
        return specialityId;
    }

    public void setSpecialityId(LongFilter specialityId) {
        this.specialityId = specialityId;
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
        final TeacherCriteria that = (TeacherCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(introduction, that.introduction) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(educationLevelId, that.educationLevelId) &&
            Objects.equals(professionId, that.professionId) &&
            Objects.equals(specialityId, that.specialityId) &&
            Objects.equals(subjectCycleId, that.subjectCycleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, introduction, personId, educationLevelId, professionId, specialityId, subjectCycleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (introduction != null ? "introduction=" + introduction + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            (educationLevelId != null ? "educationLevelId=" + educationLevelId + ", " : "") +
            (professionId != null ? "professionId=" + professionId + ", " : "") +
            (specialityId != null ? "specialityId=" + specialityId + ", " : "") +
            (subjectCycleId != null ? "subjectCycleId=" + subjectCycleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
