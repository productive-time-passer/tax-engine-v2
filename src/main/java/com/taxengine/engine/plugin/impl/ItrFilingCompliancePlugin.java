package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class ItrFilingCompliancePlugin extends AbstractRuleDrivenPlugin {

    public ItrFilingCompliancePlugin() {
        super("ItrFilingCompliancePlugin", PrimitiveType.COMPLIANCE, Set.of(ForeignTaxCreditPlugin)ForeignTaxCreditPlugin, FactType.ITR_FILED);
    }
}
