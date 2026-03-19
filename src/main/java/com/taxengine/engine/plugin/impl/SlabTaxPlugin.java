package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class SlabTaxPlugin extends AbstractRuleDrivenPlugin {

    public SlabTaxPlugin() {
        super("SlabTaxPlugin", PrimitiveType.TAX_RATE, Set.of(CarryForwardLossPlugin)CarryForwardLossPlugin, FactType.SALARY);
    }
}
