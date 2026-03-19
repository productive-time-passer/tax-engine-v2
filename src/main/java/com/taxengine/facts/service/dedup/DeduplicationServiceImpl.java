package com.taxengine.facts.service.dedup;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.pipeline.DeduplicationService;
import com.taxengine.facts.repository.FactRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class DeduplicationServiceImpl implements DeduplicationService {
    private final FactRepository factRepository;
    private final DedupHashingStrategy hashingStrategy;

    public DeduplicationServiceImpl(FactRepository factRepository, DedupHashingStrategy hashingStrategy) {
        this.factRepository = factRepository;
        this.hashingStrategy = hashingStrategy;
    }

    @Override
    public List<CanonicalFactCandidate> deduplicate(List<CanonicalFactCandidate> candidates) {
        var seen = new LinkedHashSet<String>();
        var unique = new ArrayList<CanonicalFactCandidate>();

        for (CanonicalFactCandidate candidate : candidates) {
            String hash = dedupHash(candidate);
            if (!seen.contains(hash) && !factRepository.existsByDedupHash(hash)) {
                seen.add(hash);
                unique.add(candidate);
            }
        }
        return unique;
    }

    @Override
    public String dedupHash(CanonicalFactCandidate candidate) {
        return hashingStrategy.hash(candidate);
    }
}
