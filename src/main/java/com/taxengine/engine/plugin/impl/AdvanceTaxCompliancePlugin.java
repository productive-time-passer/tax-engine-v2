package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.AbstractRuleDrivenPlugin;

import java.util.Set;

public class AdvanceTaxCompliancePlugin extends AbstractRuleDrivenPlugin {

    public AdvanceTaxCompliancePlugin() {
        super("AdvanceTaxCompliancePlugin", PrimitiveType.COMPLIANCE, Set.of(ItrFilingCompliancePlugin)ItrFilingCompliancePlugin, FactType.COMPLIANCE_EVENT);
    }
}
