package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class AgricultureIncomePlugin extends AbstractRuleDrivenPlugin {

    public AgricultureIncomePlugin() {
        super("AgricultureIncomePlugin", PrimitiveType.EXEMPTION, Set.of(RetirementBenefitExemptionPlugin)RetirementBenefitExemptionPlugin, FactType.AGRICULTURE);
    }
}
