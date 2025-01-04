package com.tchoupe.crenoflow.service.criteria;

import com.tchoupe.crenoflow.domain.enumeration.CourseFormat;
import com.tchoupe.crenoflow.domain.enumeration.SlotStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.tchoupe.crenoflow.domain.Availability} entity. This class is used
 * in {@link com.tchoupe.crenoflow.web.rest.AvailabilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /availabilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvailabilityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CourseFormat
     */
    public static class CourseFormatFilter extends Filter<CourseFormat> {

        public CourseFormatFilter() {}

        public CourseFormatFilter(CourseFormatFilter filter) {
            super(filter);
        }

        @Override
        public CourseFormatFilter copy() {
            return new CourseFormatFilter(this);
        }
    }

    /**
     * Class for filtering SlotStatus
     */
    public static class SlotStatusFilter extends Filter<SlotStatus> {

        public SlotStatusFilter() {}

        public SlotStatusFilter(SlotStatusFilter filter) {
            super(filter);
        }

        @Override
        public SlotStatusFilter copy() {
            return new SlotStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private StringFilter startTime;

    private StringFilter endTime;

    private CourseFormatFilter format;

    private StringFilter comment;

    private StringFilter videoLink;

    private StringFilter address;

    private SlotStatusFilter status;

    private InstantFilter creationDate;

    private Boolean distinct;

    public AvailabilityCriteria() {}

    public AvailabilityCriteria(AvailabilityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.format = other.format == null ? null : other.format.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.videoLink = other.videoLink == null ? null : other.videoLink.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AvailabilityCriteria copy() {
        return new AvailabilityCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getStartTime() {
        return startTime;
    }

    public StringFilter startTime() {
        if (startTime == null) {
            startTime = new StringFilter();
        }
        return startTime;
    }

    public void setStartTime(StringFilter startTime) {
        this.startTime = startTime;
    }

    public StringFilter getEndTime() {
        return endTime;
    }

    public StringFilter endTime() {
        if (endTime == null) {
            endTime = new StringFilter();
        }
        return endTime;
    }

    public void setEndTime(StringFilter endTime) {
        this.endTime = endTime;
    }

    public CourseFormatFilter getFormat() {
        return format;
    }

    public CourseFormatFilter format() {
        if (format == null) {
            format = new CourseFormatFilter();
        }
        return format;
    }

    public void setFormat(CourseFormatFilter format) {
        this.format = format;
    }

    public StringFilter getComment() {
        return comment;
    }

    public StringFilter comment() {
        if (comment == null) {
            comment = new StringFilter();
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public StringFilter getVideoLink() {
        return videoLink;
    }

    public StringFilter videoLink() {
        if (videoLink == null) {
            videoLink = new StringFilter();
        }
        return videoLink;
    }

    public void setVideoLink(StringFilter videoLink) {
        this.videoLink = videoLink;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public SlotStatusFilter getStatus() {
        return status;
    }

    public SlotStatusFilter status() {
        if (status == null) {
            status = new SlotStatusFilter();
        }
        return status;
    }

    public void setStatus(SlotStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            creationDate = new InstantFilter();
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
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
        final AvailabilityCriteria that = (AvailabilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(format, that.format) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(videoLink, that.videoLink) &&
            Objects.equals(address, that.address) &&
            Objects.equals(status, that.status) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, startTime, endTime, format, comment, videoLink, address, status, creationDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvailabilityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (format != null ? "format=" + format + ", " : "") +
            (comment != null ? "comment=" + comment + ", " : "") +
            (videoLink != null ? "videoLink=" + videoLink + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
