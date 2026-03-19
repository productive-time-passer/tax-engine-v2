package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class SpecialIncomeTaxPlugin extends AbstractRuleDrivenPlugin {

    public SpecialIncomeTaxPlugin() {
        super("SpecialIncomeTaxPlugin", PrimitiveType.TAX_RATE, Set.of(CapitalGainTaxPlugin)CapitalGainTaxPlugin, FactType.SPECIAL_INCOME);
    }
}
