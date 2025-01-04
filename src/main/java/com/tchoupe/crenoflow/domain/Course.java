package com.tchoupe.crenoflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "teachers_comment")
    private String teachersComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subjectCycles" }, allowSetters = true)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subjectCycles" }, allowSetters = true)
    private Cycle cycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "manager" }, allowSetters = true)
    private CourseLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "person", "educationLevel", "profession", "speciality", "subjectCycles" }, allowSetters = true)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Availability availability;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeachersComment() {
        return this.teachersComment;
    }

    public Course teachersComment(String teachersComment) {
        this.setTeachersComment(teachersComment);
        return this;
    }

    public void setTeachersComment(String teachersComment) {
        this.teachersComment = teachersComment;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Course subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    public Cycle getCycle() {
        return this.cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Course cycle(Cycle cycle) {
        this.setCycle(cycle);
        return this;
    }

    public CourseLocation getLocation() {
        return this.location;
    }

    public void setLocation(CourseLocation courseLocation) {
        this.location = courseLocation;
    }

    public Course location(CourseLocation courseLocation) {
        this.setLocation(courseLocation);
        return this;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course teacher(Teacher teacher) {
        this.setTeacher(teacher);
        return this;
    }

    public Availability getAvailability() {
        return this.availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Course availability(Availability availability) {
        this.setAvailability(availability);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return getId() != null && getId().equals(((Course) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", teachersComment='" + getTeachersComment() + "'" +
            "}";
    }
}
