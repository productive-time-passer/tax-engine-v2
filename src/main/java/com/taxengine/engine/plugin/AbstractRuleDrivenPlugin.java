package com.taxengine.engine.plugin;

import com.taxengine.engine.domain.TaxComputation;
import com.taxengine.engine.domain.TaxComputationState;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxImpact;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.domain.enums.TaxImpactType;
import com.taxengine.engine.rule.RuleConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractRuleDrivenPlugin implements Plugin {

    private final String pluginId;
    private final PrimitiveType primitiveType;
    private final Set<String> dependencies;
    private final FactType defaultFactType;

    protected AbstractRuleDrivenPlugin(String pluginId, PrimitiveType primitiveType, Set<String> dependencies, FactType defaultFactType) {
        this.pluginId = pluginId;
        this.primitiveType = primitiveType;
        this.dependencies = Set.copyOf(dependencies);
        this.defaultFactType = defaultFactType;
    }

    @Override
    public String pluginId() {
        return pluginId;
    }

    @Override
    public PrimitiveType primitive() {
        return primitiveType;
    }

    @Override
    public Set<String> dependencies() {
        return dependencies;
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = rules.stream()
                .sorted(Comparator.comparing(RuleConfig::ruleId))
                .map(rule -> compute(rule, context))
                .toList();
        return new PluginResult(computations);
    }

    private TaxComputation compute(RuleConfig rule, TaxContext context) {
        FactType factType = resolveFactType(rule);
        BigDecimal base = context.factIndex().totalByType(factType);
        BigDecimal threshold = readDecimal(rule.parameters(), "threshold", BigDecimal.ZERO);
        BigDecimal limit = readDecimal(rule.parameters(), "limit", new BigDecimal("999999999999"));
        BigDecimal cap = readDecimal(rule.parameters(), "cap", limit);

        BigDecimal eligible = base.subtract(threshold).max(BigDecimal.ZERO).min(limit);
        BigDecimal allowed = eligible.min(cap);

        BigDecimal amount = primitiveType == PrimitiveType.TAX_RATE
                ? computeTax(rule.parameters(), allowed)
                : allowed;

        if (primitiveType == PrimitiveType.COMPLIANCE) {
            boolean compliant = base.compareTo(BigDecimal.ZERO) > 0;
            amount = compliant ? BigDecimal.ZERO : readDecimal(rule.parameters(), "penalty", BigDecimal.ZERO);
            allowed = amount;
            eligible = amount;
        }

        TaxImpact impact = new TaxImpact(
                primitiveType == PrimitiveType.TAX_CREDIT ? TaxImpactType.CREDIT : TaxImpactType.COMPUTED,
                primitiveType,
                pluginId,
                rule.section(),
                amount.setScale(2, RoundingMode.HALF_UP),
                eligible.setScale(2, RoundingMode.HALF_UP),
                allowed.setScale(2, RoundingMode.HALF_UP),
                Map.of("factType", factType.name())
        );

        return new TaxComputation(
                UUID.nameUUIDFromBytes((pluginId + rule.ruleId() + context.taxpayer().taxpayerId()).getBytes()).toString(),
                pluginId,
                rule.ruleId(),
                rule.section(),
                primitiveType,
                Map.of("base", base, "threshold", threshold),
                List.of(impact),
                "Rule-driven deterministic computation",
                Instant.EPOCH
        );
    }

    @SuppressWarnings("unchecked")
    private BigDecimal computeTax(Map<String, Object> params, BigDecimal taxableAmount) {
        Object slabsObject = params.get("slabs");
        if (slabsObject instanceof List<?> slabs && !slabs.isEmpty()) {
            BigDecimal tax = BigDecimal.ZERO;
            BigDecimal remaining = taxableAmount;
            BigDecimal lower = BigDecimal.ZERO;
            for (Object slabObj : slabs) {
                Map<String, Object> slab = (Map<String, Object>) slabObj;
                BigDecimal upto = readDecimal(slab, "upto", remaining.add(lower));
                BigDecimal rate = readDecimal(slab, "rate", BigDecimal.ZERO);
                BigDecimal slabWidth = upto.subtract(lower);
                BigDecimal taxableInSlab = remaining.min(slabWidth).max(BigDecimal.ZERO);
                tax = tax.add(taxableInSlab.multiply(rate));
                remaining = remaining.subtract(taxableInSlab);
                lower = upto;
                if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }
            return tax;
        }
        BigDecimal rate = readDecimal(params, "rate", BigDecimal.ZERO);
        return taxableAmount.multiply(rate);
    }

    private FactType resolveFactType(RuleConfig rule) {
        Object value = rule.parameters().get("factType");
        if (value instanceof String factTypeString) {
            return FactType.valueOf(factTypeString);
        }
        return defaultFactType;
    }

    private BigDecimal readDecimal(Map<String, Object> params, String key, BigDecimal defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return new BigDecimal(value.toString());
    }
}
