package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class CapitalLossSetoffPlugin extends AbstractRuleDrivenPlugin {

    public CapitalLossSetoffPlugin() {
        super("CapitalLossSetoffPlugin", PrimitiveType.ADJUSTMENT, Set.of(LossSetoffPlugin)LossSetoffPlugin, FactType.CAPITAL_LOSS);
    }
}
