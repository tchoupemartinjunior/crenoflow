package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TeacherRepositoryWithBagRelationshipsImpl implements TeacherRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Teacher> fetchBagRelationships(Optional<Teacher> teacher) {
        return teacher.map(this::fetchSubjectCycles);
    }

    @Override
    public Page<Teacher> fetchBagRelationships(Page<Teacher> teachers) {
        return new PageImpl<>(fetchBagRelationships(teachers.getContent()), teachers.getPageable(), teachers.getTotalElements());
    }

    @Override
    public List<Teacher> fetchBagRelationships(List<Teacher> teachers) {
        return Optional.of(teachers).map(this::fetchSubjectCycles).orElse(Collections.emptyList());
    }

    Teacher fetchSubjectCycles(Teacher result) {
        return entityManager
            .createQuery("select teacher from Teacher teacher left join fetch teacher.subjectCycles where teacher.id = :id", Teacher.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Teacher> fetchSubjectCycles(List<Teacher> teachers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, teachers.size()).forEach(index -> order.put(teachers.get(index).getId(), index));
        List<Teacher> result = entityManager
            .createQuery(
                "select teacher from Teacher teacher left join fetch teacher.subjectCycles where teacher in :teachers",
                Teacher.class
            )
            .setParameter("teachers", teachers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
