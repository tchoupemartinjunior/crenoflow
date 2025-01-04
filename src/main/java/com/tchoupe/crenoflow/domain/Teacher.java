package com.tchoupe.crenoflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Teacher.
 */
@Entity
@Table(name = "teacher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "introduction")
    private String introduction;

    @JsonIgnoreProperties(value = { "user", "schoolLevel", "teacher", "photoPerson" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    private EducationLevel educationLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profession profession;

    @ManyToOne(fetch = FetchType.LAZY)
    private Speciality speciality;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_teacher__subject_cycle",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_cycle_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "teachers", "subjects", "cycles" }, allowSetters = true)
    private Set<SubjectCycle> subjectCycles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Teacher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public Teacher introduction(String introduction) {
        this.setIntroduction(introduction);
        return this;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Teacher person(Person person) {
        this.setPerson(person);
        return this;
    }

    public EducationLevel getEducationLevel() {
        return this.educationLevel;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Teacher educationLevel(EducationLevel educationLevel) {
        this.setEducationLevel(educationLevel);
        return this;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Teacher profession(Profession profession) {
        this.setProfession(profession);
        return this;
    }

    public Speciality getSpeciality() {
        return this.speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Teacher speciality(Speciality speciality) {
        this.setSpeciality(speciality);
        return this;
    }

    public Set<SubjectCycle> getSubjectCycles() {
        return this.subjectCycles;
    }

    public void setSubjectCycles(Set<SubjectCycle> subjectCycles) {
        this.subjectCycles = subjectCycles;
    }

    public Teacher subjectCycles(Set<SubjectCycle> subjectCycles) {
        this.setSubjectCycles(subjectCycles);
        return this;
    }

    public Teacher addSubjectCycle(SubjectCycle subjectCycle) {
        this.subjectCycles.add(subjectCycle);
        return this;
    }

    public Teacher removeSubjectCycle(SubjectCycle subjectCycle) {
        this.subjectCycles.remove(subjectCycle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Teacher)) {
            return false;
        }
        return getId() != null && getId().equals(((Teacher) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Teacher{" +
            "id=" + getId() +
            ", introduction='" + getIntroduction() + "'" +
            "}";
    }
}
