package com.tchoupe.crenoflow.domain;

import com.tchoupe.crenoflow.domain.enumeration.CourseFormat;
import com.tchoupe.crenoflow.domain.enumeration.SlotStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Availability.
 */
@Entity
@Table(name = "availability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Availability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$")
    @Column(name = "start_time", nullable = false)
    private String startTime;

    @NotNull
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$")
    @Column(name = "end_time", nullable = false)
    private String endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    private CourseFormat format;

    @Column(name = "comment")
    private String comment;

    @Column(name = "video_link")
    private String videoLink;

    @Column(name = "address")
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SlotStatus status;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Availability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Availability date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public Availability startTime(String startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public Availability endTime(String endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public CourseFormat getFormat() {
        return this.format;
    }

    public Availability format(CourseFormat format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(CourseFormat format) {
        this.format = format;
    }

    public String getComment() {
        return this.comment;
    }

    public Availability comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVideoLink() {
        return this.videoLink;
    }

    public Availability videoLink(String videoLink) {
        this.setVideoLink(videoLink);
        return this;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getAddress() {
        return this.address;
    }

    public Availability address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SlotStatus getStatus() {
        return this.status;
    }

    public Availability status(SlotStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Availability creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Availability)) {
            return false;
        }
        return getId() != null && getId().equals(((Availability) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Availability{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", format='" + getFormat() + "'" +
            ", comment='" + getComment() + "'" +
            ", videoLink='" + getVideoLink() + "'" +
            ", address='" + getAddress() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
