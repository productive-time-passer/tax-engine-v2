package com.taxengine.facts.service.fact;

import com.taxengine.facts.domain.model.Fact;
import com.taxengine.facts.repository.FactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FactServiceImpl implements FactService {
    private final FactRepository factRepository;

    public FactServiceImpl(FactRepository factRepository) {
        this.factRepository = factRepository;
    }

    @Override
    public List<Fact> getFactsForTaxpayer(UUID taxpayerId) {
        return factRepository.findByTaxpayerIdOrderByCreatedAtDesc(taxpayerId);
    }
}
