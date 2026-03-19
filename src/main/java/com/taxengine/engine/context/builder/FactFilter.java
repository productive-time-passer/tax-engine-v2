package com.taxengine.engine.context.builder;

import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.domain.TaxPeriod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FactFilter {

    private static final Comparator<Fact> FACT_ORDER = Comparator
            .comparing(Fact::createdAt, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(Fact::factId, Comparator.nullsFirst(Comparator.naturalOrder()));

    public List<Fact> filterRelevantFacts(List<Fact> facts, TaxPeriod period) {
        return filterRelevantFacts(facts, period, null);
    }

    public List<Fact> filterRelevantFacts(List<Fact> facts, TaxPeriod period, BigDecimal minConfidenceScore) {
        if (facts == null || facts.isEmpty()) {
            return List.of();
        }

        String financialYear = period.financialYear();
        List<Fact> filtered = new ArrayList<>(facts.size());
        for (int i = 0; i < facts.size(); i++) {
            Fact fact = facts.get(i);
            if (!fact.active()) {
                continue;
            }
            if (!financialYear.equals(fact.financialYear())) {
                continue;
            }
            if (minConfidenceScore != null && fact.confidenceScore() != null
                    && fact.confidenceScore().compareTo(minConfidenceScore) < 0) {
                continue;
            }
            filtered.add(fact);
        }
        filtered.sort(FACT_ORDER);
        return List.copyOf(filtered);
    }
}
