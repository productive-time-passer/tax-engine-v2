package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class InsuranceDeductionPlugin extends AbstractRuleDrivenPlugin {

    public InsuranceDeductionPlugin() {
        super("InsuranceDeductionPlugin", PrimitiveType.DEDUCTION, Set.of(InvestmentDeductionPlugin)InvestmentDeductionPlugin, FactType.INSURANCE);
    }
}
