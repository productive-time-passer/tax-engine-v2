package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class HousePropertyIncomePlugin extends AbstractRuleDrivenPlugin {

    public HousePropertyIncomePlugin() {
        super("HousePropertyIncomePlugin", PrimitiveType.INCOME, Set.of(SalaryIncomeClassificationPlugin)SalaryIncomeClassificationPlugin, FactType.HOUSE_PROPERTY);
    }
}
