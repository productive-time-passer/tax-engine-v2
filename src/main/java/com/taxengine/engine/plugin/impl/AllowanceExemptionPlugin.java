package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.*;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.domain.enums.TaxImpactType;
import com.taxengine.engine.plugin.Plugin;
import com.taxengine.engine.plugin.PluginComputationFactory;
import com.taxengine.engine.plugin.PluginResult;
import com.taxengine.engine.rule.RuleConfig;

import java.math.BigDecimal;
import java.util.*;

public class AllowanceExemptionPlugin implements Plugin {
    private static final Set<String> METRO_CITIES = Set.of("DELHI", "MUMBAI", "CHENNAI", "KOLKATA");
    private static final BigDecimal METRO_PERCENT = new BigDecimal("0.50");
    private static final BigDecimal NON_METRO_PERCENT = new BigDecimal("0.40");
    private static final BigDecimal TEN_PERCENT = new BigDecimal("0.10");
    private static final BigDecimal LANDLORD_PAN_THRESHOLD = new BigDecimal("100000");

    @Override
    public String pluginId() {
        return "AllowanceExemptionPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.EXEMPTION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("OtherSourcesIncomePlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return "OLD".equalsIgnoreCase(context.taxPeriod().regime());
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        if (rules.isEmpty()) {
            return new PluginResult(List.of());
        }

        for (Fact allowanceFact : context.factIndex().byType(FactType.ALLOWANCE)) {
            if (!isHraFact(allowanceFact)) {
                continue;
            }
            if (!isEligibleClaim(allowanceFact)) {
                continue;
            }

            BigDecimal actualHra = amount(allowanceFact, "actualHraReceived", allowanceFact.amount());
            BigDecimal salaryForHra = salaryForHra(allowanceFact, context);
            BigDecimal annualRentPaid = amount(allowanceFact, "annualRentPaid", amount(allowanceFact, "rentPaid", BigDecimal.ZERO));

            if (actualHra.compareTo(BigDecimal.ZERO) <= 0 || salaryForHra.compareTo(BigDecimal.ZERO) <= 0 || annualRentPaid.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (!hasRequiredDocumentation(allowanceFact, annualRentPaid)) {
                continue;
            }

            BigDecimal percentageCap = isMetro(allowanceFact) ? METRO_PERCENT : NON_METRO_PERCENT;
            BigDecimal salaryPercentageExemption = salaryForHra.multiply(percentageCap);
            BigDecimal rentMinusTenPercent = annualRentPaid.subtract(salaryForHra.multiply(TEN_PERCENT)).max(BigDecimal.ZERO);
            BigDecimal exemption = actualHra.min(salaryPercentageExemption).min(rentMinusTenPercent);

            RuleConfig rule = rules.getFirst();
            Map<String, Object> inputs = new LinkedHashMap<>();
            inputs.put("actualHraReceived", actualHra);
            inputs.put("salaryForHra", salaryForHra);
            inputs.put("annualRentPaid", annualRentPaid);
            inputs.put("isMetro", isMetro(allowanceFact));
            inputs.put("salaryPercentageCap", salaryPercentageExemption);
            inputs.put("rentMinusTenPercentSalary", rentMinusTenPercent);

            computations.add(PluginComputationFactory.singleImpact(
                    pluginId(),
                    primitive(),
                    rule,
                    inputs,
                    "EXEMPTION",
                    "HRA",
                    exemption,
                    actualHra,
                    exemption,
                    TaxImpactType.COMPUTED,
                    "HRA exemption computed under Section 10(13A) for old regime",
                    context.taxpayer().taxpayerId()
            ));
        }

        return new PluginResult(computations);
    }

    private boolean isHraFact(Fact fact) {
        Object allowanceType = fact.attributes().get("allowanceType");
        if (allowanceType == null) {
            return true;
        }
        return "HRA".equalsIgnoreCase(String.valueOf(allowanceType));
    }

    private boolean isEligibleClaim(Fact fact) {
        if (flag(fact, "ownsResidence", false)) {
            return false;
        }
        if (!flag(fact, "isRentedAccommodation", true)) {
            return false;
        }
        String paidToRelationship = String.valueOf(fact.attributes().getOrDefault("paidToRelationship", "LANDLORD"));
        if ("SPOUSE".equalsIgnoreCase(paidToRelationship)) {
            return false;
        }
        if ("PARENT".equalsIgnoreCase(paidToRelationship)) {
            return flag(fact, "hasBankTransferProof", false);
        }
        return true;
    }

    private boolean hasRequiredDocumentation(Fact fact, BigDecimal annualRentPaid) {
        if (!flag(fact, "hasRentReceipts", true) || !flag(fact, "hasRentalAgreement", true)) {
            return false;
        }
        if (annualRentPaid.compareTo(LANDLORD_PAN_THRESHOLD) > 0) {
            String landlordPan = String.valueOf(fact.attributes().getOrDefault("landlordPan", ""));
            return !landlordPan.isBlank();
        }
        return true;
    }

    private BigDecimal salaryForHra(Fact allowanceFact, TaxContext context) {
        BigDecimal explicit = amount(allowanceFact, "salaryForHra", null);
        if (explicit != null) {
            return explicit;
        }
        BigDecimal basic = amount(allowanceFact, "basicSalary", BigDecimal.ZERO);
        BigDecimal dearnessAllowance = amount(allowanceFact, "dearnessAllowance", BigDecimal.ZERO);
        if (basic.compareTo(BigDecimal.ZERO) > 0 || dearnessAllowance.compareTo(BigDecimal.ZERO) > 0) {
            return basic.add(dearnessAllowance);
        }
        return context.factIndex().byType(FactType.SALARY).stream()
                .filter(fact -> "BASIC_SALARY".equalsIgnoreCase(String.valueOf(fact.attributes().getOrDefault("component", "")))
                        || "DEARNESS_ALLOWANCE".equalsIgnoreCase(String.valueOf(fact.attributes().getOrDefault("component", ""))))
                .map(Fact::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isMetro(Fact fact) {
        if (fact.attributes().containsKey("isMetro")) {
            return flag(fact, "isMetro", false);
        }
        String city = String.valueOf(fact.attributes().getOrDefault("city", ""));
        return METRO_CITIES.contains(city.toUpperCase(Locale.ROOT));
    }

    private boolean flag(Fact fact, String key, boolean defaultValue) {
        Object raw = fact.attributes().get(key);
        if (raw == null) {
            return defaultValue;
        }
        if (raw instanceof Boolean b) {
            return b;
        }
        return Boolean.parseBoolean(String.valueOf(raw));
    }

    private BigDecimal amount(Fact fact, String key, BigDecimal defaultValue) {
        Object raw = fact.attributes().get(key);
        if (raw == null) {
            return defaultValue;
        }
        if (raw instanceof BigDecimal decimal) {
            return decimal;
        }
        if (raw instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return new BigDecimal(String.valueOf(raw));
    }
}
