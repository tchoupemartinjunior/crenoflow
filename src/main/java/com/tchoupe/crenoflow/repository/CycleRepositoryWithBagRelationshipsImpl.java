package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.Cycle;
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
public class CycleRepositoryWithBagRelationshipsImpl implements CycleRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Cycle> fetchBagRelationships(Optional<Cycle> cycle) {
        return cycle.map(this::fetchSubjectCycles);
    }

    @Override
    public Page<Cycle> fetchBagRelationships(Page<Cycle> cycles) {
        return new PageImpl<>(fetchBagRelationships(cycles.getContent()), cycles.getPageable(), cycles.getTotalElements());
    }

    @Override
    public List<Cycle> fetchBagRelationships(List<Cycle> cycles) {
        return Optional.of(cycles).map(this::fetchSubjectCycles).orElse(Collections.emptyList());
    }

    Cycle fetchSubjectCycles(Cycle result) {
        return entityManager
            .createQuery("select cycle from Cycle cycle left join fetch cycle.subjectCycles where cycle.id = :id", Cycle.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Cycle> fetchSubjectCycles(List<Cycle> cycles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cycles.size()).forEach(index -> order.put(cycles.get(index).getId(), index));
        List<Cycle> result = entityManager
            .createQuery("select cycle from Cycle cycle left join fetch cycle.subjectCycles where cycle in :cycles", Cycle.class)
            .setParameter("cycles", cycles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
