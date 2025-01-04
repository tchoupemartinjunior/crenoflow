package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.Cycle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CycleRepositoryWithBagRelationships {
    Optional<Cycle> fetchBagRelationships(Optional<Cycle> cycle);

    List<Cycle> fetchBagRelationships(List<Cycle> cycles);

    Page<Cycle> fetchBagRelationships(Page<Cycle> cycles);
}
