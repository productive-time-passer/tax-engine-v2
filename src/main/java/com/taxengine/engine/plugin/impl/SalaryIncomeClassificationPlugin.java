package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class SalaryIncomeClassificationPlugin extends AbstractRuleDrivenPlugin {

    public SalaryIncomeClassificationPlugin() {
        super("SalaryIncomeClassificationPlugin", PrimitiveType.INCOME, Set.of(), FactType.SALARY);
    }
}
