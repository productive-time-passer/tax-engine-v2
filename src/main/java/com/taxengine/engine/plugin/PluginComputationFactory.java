package com.taxengine.engine.plugin;

import com.taxengine.engine.domain.TaxComputation;
import com.taxengine.engine.domain.TaxImpact;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.domain.enums.TaxImpactType;
import com.taxengine.engine.rule.RuleConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PluginComputationFactory {

    private PluginComputationFactory() {
    }

    public static TaxComputation singleImpact(String pluginId,
                                              PrimitiveType primitiveType,
                                              RuleConfig rule,
                                              Map<String, Object> inputs,
                                              String category,
                                              String subCategory,
                                              BigDecimal amount,
                                              BigDecimal eligible,
                                              BigDecimal allowed,
                                              TaxImpactType impactType,
                                              String explanation,
                                              String taxpayerId) {
        TaxImpact impact = new TaxImpact(
                impactType,
                primitiveType,
                category,
                subCategory,
                amount.setScale(2, RoundingMode.HALF_UP),
                eligible.setScale(2, RoundingMode.HALF_UP),
                allowed.setScale(2, RoundingMode.HALF_UP),
                Map.of("ruleId", rule.ruleId())
        );
        return new TaxComputation(
                deterministicId(pluginId, rule.ruleId(), taxpayerId, subCategory),
                pluginId,
                rule.ruleId(),
                rule.section(),
                primitiveType,
                inputs,
                List.of(impact),
                explanation,
                Instant.EPOCH
        );
    }

    private static String deterministicId(String pluginId, String ruleId, String taxpayerId, String discriminator) {
        return UUID.nameUUIDFromBytes((pluginId + "|" + ruleId + "|" + taxpayerId + "|" + discriminator).getBytes()).toString();
    }
}
