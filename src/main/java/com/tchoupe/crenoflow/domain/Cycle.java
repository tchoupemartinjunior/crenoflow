package com.tchoupe.crenoflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cycle.
 */
@Entity
@Table(name = "cycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_cycle__subject_cycle",
        joinColumns = @JoinColumn(name = "cycle_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_cycle_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "teachers", "subjects", "cycles" }, allowSetters = true)
    private Set<SubjectCycle> subjectCycles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Cycle label(String label) {
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

    public Cycle subjectCycles(Set<SubjectCycle> subjectCycles) {
        this.setSubjectCycles(subjectCycles);
        return this;
    }

    public Cycle addSubjectCycle(SubjectCycle subjectCycle) {
        this.subjectCycles.add(subjectCycle);
        return this;
    }

    public Cycle removeSubjectCycle(SubjectCycle subjectCycle) {
        this.subjectCycles.remove(subjectCycle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cycle)) {
            return false;
        }
        return getId() != null && getId().equals(((Cycle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cycle{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
