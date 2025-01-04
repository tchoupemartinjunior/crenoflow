package com.tchoupe.crenoflow.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.tchoupe.crenoflow.domain.PhotoPerson} entity. This class is used
 * in {@link com.tchoupe.crenoflow.web.rest.PhotoPersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /photo-people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhotoPersonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter personId;

    private Boolean distinct;

    public PhotoPersonCriteria() {}

    public PhotoPersonCriteria(PhotoPersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PhotoPersonCriteria copy() {
        return new PhotoPersonCriteria(this);
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
        final PhotoPersonCriteria that = (PhotoPersonCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(personId, that.personId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhotoPersonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
