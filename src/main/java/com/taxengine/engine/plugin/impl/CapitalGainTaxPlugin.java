package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class CapitalGainTaxPlugin extends AbstractRuleDrivenPlugin {

    public CapitalGainTaxPlugin() {
        super("CapitalGainTaxPlugin", PrimitiveType.TAX_RATE, Set.of(SlabTaxPlugin)SlabTaxPlugin, FactType.CAPITAL_GAIN);
    }
}
