package com.taxengine.engine.domain;

import com.taxengine.engine.domain.enums.PrimitiveType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TaxComputationState {

    private final List<TaxComputation> computations;
    private final Map<PrimitiveType, List<TaxImpact>> impactsByPrimitive;
    private final Map<PrimitiveType, BigDecimal> totalsByPrimitive;

    public TaxComputationState() {
        this(List.of());
    }

    private TaxComputationState(List<TaxComputation> computations) {
        this.computations = List.copyOf(computations);
        this.impactsByPrimitive = Collections.unmodifiableMap(computations.stream()
                .flatMap(c -> c.impacts().stream())
                .collect(Collectors.groupingBy(TaxImpact::primitiveType, () -> new EnumMap<>(PrimitiveType.class), Collectors.toUnmodifiableList())));
        Map<PrimitiveType, BigDecimal> mutableTotals = new EnumMap<>(PrimitiveType.class);
        impactsByPrimitive.forEach((k, v) -> mutableTotals.put(k, v.stream().map(TaxImpact::allowedAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));
        this.totalsByPrimitive = Collections.unmodifiableMap(mutableTotals);
    }

    public TaxComputationState addComputations(List<TaxComputation> newEntries) {
        return new TaxComputationState(Stream.concat(this.computations.stream(), newEntries.stream()).toList());
    }

    public BigDecimal getTotalByPrimitive(PrimitiveType primitiveType) {
        return totalsByPrimitive.getOrDefault(primitiveType, BigDecimal.ZERO);
    }

    public List<TaxComputation> computations() {
        return computations;
    }

    public Map<PrimitiveType, List<TaxImpact>> impactsByPrimitive() {
        return impactsByPrimitive;
    }
}
