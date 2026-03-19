package com.taxengine.advisory.rule;

import com.taxengine.engine.domain.Fact;
import com.taxengine.engine.domain.TaxComputation;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public abstract class AdvisoryRuleSupport {

    protected BigDecimal totalFactAmount(TaxContext context, FactType factType) {
        return context.factIndex().totalByType(factType);
    }

    protected List<Fact> facts(TaxContext context, FactType factType) {
        return context.factIndex().byType(factType);
    }

    protected BigDecimal numericAttribute(Fact fact, String key) {
        Object value = fact.attributes().get(key);
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        if (value instanceof String s) {
            try {
                return new BigDecimal(s);
            } catch (NumberFormatException ignored) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    protected BigDecimal totalAllowedAmount(List<TaxComputation> ledger, PrimitiveType primitiveType, String categoryHint) {
        return ledger.stream()
                .filter(c -> c.primitiveType() == primitiveType)
                .flatMap(c -> c.impacts().stream())
                .filter(i -> i.category() != null && i.category().toLowerCase().contains(categoryHint.toLowerCase()))
                .map(i -> Optional.ofNullable(i.allowedAmount()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
