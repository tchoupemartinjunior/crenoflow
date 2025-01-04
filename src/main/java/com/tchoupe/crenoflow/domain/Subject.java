package com.tchoupe.crenoflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "label", length = 64, nullable = false)
    private String label;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_subject__subject_cycle",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_cycle_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "teachers", "subjects", "cycles" }, allowSetters = true)
    private Set<SubjectCycle> subjectCycles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Subject label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<SubjectCycle> getSubjectCycles() {
        return this.subjectCycles;
    }

    public void setSubjectCycles(Set<SubjectCycle> subjectCycles) {
        this.subjectCycles = subjectCycles;
    }

    public Subject subjectCycles(Set<SubjectCycle> subjectCycles) {
        this.setSubjectCycles(subjectCycles);
        return this;
    }

    public Subject addSubjectCycle(SubjectCycle subjectCycle) {
        this.subjectCycles.add(subjectCycle);
        return this;
    }

    public Subject removeSubjectCycle(SubjectCycle subjectCycle) {
        this.subjectCycles.remove(subjectCycle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return getId() != null && getId().equals(((Subject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
