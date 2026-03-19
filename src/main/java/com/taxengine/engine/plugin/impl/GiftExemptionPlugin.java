package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class GiftExemptionPlugin extends AbstractRuleDrivenPlugin {

    public GiftExemptionPlugin() {
        super("GiftExemptionPlugin", PrimitiveType.EXEMPTION, Set.of(AgricultureIncomePlugin)AgricultureIncomePlugin, FactType.GIFT);
    }
}
