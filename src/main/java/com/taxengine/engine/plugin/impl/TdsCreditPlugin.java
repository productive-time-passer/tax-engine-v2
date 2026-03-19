package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class TdsCreditPlugin extends AbstractRuleDrivenPlugin {

    public TdsCreditPlugin() {
        super("TdsCreditPlugin", PrimitiveType.TAX_CREDIT, Set.of(CryptoTaxPlugin)CryptoTaxPlugin, FactType.TDS);
    }
}
