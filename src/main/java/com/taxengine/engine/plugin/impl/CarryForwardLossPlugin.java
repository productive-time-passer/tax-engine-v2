package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class CarryForwardLossPlugin extends AbstractRuleDrivenPlugin {

    public CarryForwardLossPlugin() {
        super("CarryForwardLossPlugin", PrimitiveType.ADJUSTMENT, Set.of(CapitalLossSetoffPlugin)CapitalLossSetoffPlugin, FactType.CARRY_FORWARD);
    }
}
