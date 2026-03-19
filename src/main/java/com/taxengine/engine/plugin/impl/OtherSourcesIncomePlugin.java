package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class OtherSourcesIncomePlugin extends AbstractRuleDrivenPlugin {

    public OtherSourcesIncomePlugin() {
        super("OtherSourcesIncomePlugin", PrimitiveType.INCOME, Set.of(CapitalGainClassificationPlugin)CapitalGainClassificationPlugin, FactType.OTHER_SOURCE);
    }
}
