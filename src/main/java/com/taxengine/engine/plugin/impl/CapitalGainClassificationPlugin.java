package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class CapitalGainClassificationPlugin extends AbstractRuleDrivenPlugin {

    public CapitalGainClassificationPlugin() {
        super("CapitalGainClassificationPlugin", PrimitiveType.INCOME, Set.of(BusinessIncomePlugin)BusinessIncomePlugin, FactType.CAPITAL_GAIN);
    }
}
