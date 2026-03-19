package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class LossSetoffPlugin extends AbstractRuleDrivenPlugin {

    public LossSetoffPlugin() {
        super("LossSetoffPlugin", PrimitiveType.ADJUSTMENT, Set.of(DonationDeductionPlugin)DonationDeductionPlugin, FactType.LOSS);
    }
}
