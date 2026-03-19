package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class CryptoTaxPlugin extends AbstractRuleDrivenPlugin {

    public CryptoTaxPlugin() {
        super("CryptoTaxPlugin", PrimitiveType.TAX_RATE, Set.of(SpecialIncomeTaxPlugin)SpecialIncomeTaxPlugin, FactType.CRYPTO);
    }
}
