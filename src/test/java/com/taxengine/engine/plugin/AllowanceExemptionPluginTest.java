package com.taxengine.engine.plugin;

import com.taxengine.engine.domain.Fact;
import com.taxengine.engine.domain.FactIndex;
import com.taxengine.engine.domain.TaxComputationState;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxPeriod;
import com.taxengine.engine.domain.Taxpayer;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.impl.AllowanceExemptionPlugin;
import com.taxengine.engine.rule.RuleConfig;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AllowanceExemptionPluginTest {

    private final AllowanceExemptionPlugin plugin = new AllowanceExemptionPlugin();
    private final RuleConfig hraRule = new RuleConfig(
            "AllowanceExemptionPlugin",
            "HRA_EXEMPTION_001",
            "10(13A)",
            PrimitiveType.EXEMPTION,
            Map.of(),
            "FY2025",
            "OLD",
            true
    );

    @Test
    void computesHraExemptionAsMinimumOfThreeLimitsForNonMetro() {
        TaxContext context = context(
                "OLD",
                new Fact("SAL-1", FactType.SALARY, "P1", new BigDecimal("360000"), Map.of("component", "BASIC_SALARY")),
                new Fact("HRA-1", FactType.ALLOWANCE, "P1", new BigDecimal("240000"), Map.of(
                        "allowanceType", "HRA",
                        "basicSalary", new BigDecimal("480000"),
                        "dearnessAllowance", BigDecimal.ZERO,
                        "annualRentPaid", new BigDecimal("180000"),
                        "city", "Pune",
                        "isRentedAccommodation", true,
                        "hasRentReceipts", true,
                        "hasRentalAgreement", true,
                        "landlordPan", "ABCDE1234F"
                ))
        );

        var result = plugin.evaluate(context, new TaxComputationState(), List.of(hraRule));
        BigDecimal exempted = result.computations().getFirst().impacts().getFirst().allowedAmount();

        assertEquals(new BigDecimal("132000.00"), exempted);
    }

    @Test
    void blocksHraExemptionInNewRegime() {
        TaxContext context = context(
                "NEW",
                new Fact("HRA-1", FactType.ALLOWANCE, "P1", new BigDecimal("100000"), Map.of(
                        "allowanceType", "HRA",
                        "salaryForHra", new BigDecimal("400000"),
                        "annualRentPaid", new BigDecimal("180000")
                ))
        );

        assertFalse(plugin.isApplicable(context));
    }

    @Test
    void rejectsClaimWhenLandlordPanMissingForRentAboveOneLakh() {
        TaxContext context = context(
                "OLD",
                new Fact("HRA-1", FactType.ALLOWANCE, "P1", new BigDecimal("200000"), Map.of(
                        "allowanceType", "HRA",
                        "salaryForHra", new BigDecimal("400000"),
                        "annualRentPaid", new BigDecimal("180000"),
                        "isRentedAccommodation", true,
                        "hasRentReceipts", true,
                        "hasRentalAgreement", true
                ))
        );

        var result = plugin.evaluate(context, new TaxComputationState(), List.of(hraRule));
        assertEquals(0, result.computations().size());
    }

    private TaxContext context(String regime, Fact... facts) {
        return new TaxContext(
                new Taxpayer("TP1", "ABCDE1234F", "RESIDENT"),
                new TaxPeriod("FY2025", "AY2026", regime),
                Map.of(),
                new FactIndex(List.of(facts))
        );
    }
}
