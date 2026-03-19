package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class InvestmentDeductionPlugin extends AbstractRuleDrivenPlugin {

    public InvestmentDeductionPlugin() {
        super("InvestmentDeductionPlugin", PrimitiveType.DEDUCTION, Set.of(GiftExemptionPlugin)GiftExemptionPlugin, FactType.INVESTMENT);
    }
}
