package com.tchoupe.crenoflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubjectCycle.
 */
@Entity
@Table(name = "subject_cycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubjectCycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjectCycles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person", "educationLevel", "profession", "speciality", "subjectCycles" }, allowSetters = true)
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjectCycles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjectCycles" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjectCycles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjectCycles" }, allowSetters = true)
    private Set<Cycle> cycles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubjectCycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Teacher> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        if (this.teachers != null) {
            this.teachers.forEach(i -> i.removeSubjectCycle(this));
        }
        if (teachers != null) {
            teachers.forEach(i -> i.addSubjectCycle(this));
        }
        this.teachers = teachers;
    }

    public SubjectCycle teachers(Set<Teacher> teachers) {
        this.setTeachers(teachers);
        return this;
    }

    public SubjectCycle addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getSubjectCycles().add(this);
        return this;
    }

    public SubjectCycle removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getSubjectCycles().remove(this);
        return this;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        if (this.subjects != null) {
            this.subjects.forEach(i -> i.removeSubjectCycle(this));
        }
        if (subjects != null) {
            subjects.forEach(i -> i.addSubjectCycle(this));
        }
        this.subjects = subjects;
    }

    public SubjectCycle subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public SubjectCycle addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getSubjectCycles().add(this);
        return this;
    }

    public SubjectCycle removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getSubjectCycles().remove(this);
        return this;
    }

    public Set<Cycle> getCycles() {
        return this.cycles;
    }

    public void setCycles(Set<Cycle> cycles) {
        if (this.cycles != null) {
            this.cycles.forEach(i -> i.removeSubjectCycle(this));
        }
        if (cycles != null) {
            cycles.forEach(i -> i.addSubjectCycle(this));
        }
        this.cycles = cycles;
    }

    public SubjectCycle cycles(Set<Cycle> cycles) {
        this.setCycles(cycles);
        return this;
    }

    public SubjectCycle addCycle(Cycle cycle) {
        this.cycles.add(cycle);
        cycle.getSubjectCycles().add(this);
        return this;
    }

    public SubjectCycle removeCycle(Cycle cycle) {
        this.cycles.remove(cycle);
        cycle.getSubjectCycles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubjectCycle)) {
            return false;
        }
        return getId() != null && getId().equals(((SubjectCycle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubjectCycle{" +
            "id=" + getId() +
            "}";
    }
}
