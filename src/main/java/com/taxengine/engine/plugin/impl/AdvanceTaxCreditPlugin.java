package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class AdvanceTaxCreditPlugin extends AbstractRuleDrivenPlugin {

    public AdvanceTaxCreditPlugin() {
        super("AdvanceTaxCreditPlugin", PrimitiveType.TAX_CREDIT, Set.of(TdsCreditPlugin)TdsCreditPlugin, FactType.ADVANCE_TAX);
    }
}
