package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class RetirementBenefitExemptionPlugin extends AbstractRuleDrivenPlugin {

    public RetirementBenefitExemptionPlugin() {
        super("RetirementBenefitExemptionPlugin", PrimitiveType.EXEMPTION, Set.of(AllowanceExemptionPlugin)AllowanceExemptionPlugin, FactType.RETIREMENT);
    }
}
