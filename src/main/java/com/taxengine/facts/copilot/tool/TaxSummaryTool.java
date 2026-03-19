package com.taxengine.facts.copilot.tool;

import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.facts.copilot.model.CopilotContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TaxSummaryTool implements CopilotTool {

    @Override
    public String name() {
        return "tax_summary";
    }

    @Override
    public String description() {
        return "Summarizes tax engine outputs for grounded response generation";
    }

    @Override
    public Object execute(CopilotContext context) {
        TaxEngineResult result = context.taxEngineResult();
        return Map.of(
                "grossIncome", result.grossIncome(),
                "deductions", result.deductions(),
                "exemptions", result.exemptions(),
                "taxableIncome", result.taxableIncome(),
                "taxBeforeCredits", result.taxBeforeCredits(),
                "taxCredits", result.taxCredits(),
                "finalTaxPayable", result.finalTaxPayable()
        );
    }
}
