package com.taxengine.facts.copilot.tool;

import com.taxengine.engine.domain.TaxComputation;
import com.taxengine.engine.domain.TaxImpact;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.facts.copilot.model.CopilotContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DeductionBreakdownTool implements CopilotTool {
    @Override
    public String name() {
        return "deduction_breakdown";
    }

    @Override
    public String description() {
        return "Returns deduction and exemption components from tax ledger";
    }

    @Override
    public Object execute(CopilotContext context) {
        List<Map<String, Object>> deductions = context.taxEngineResult().ledger().stream()
                .filter(step -> step.primitiveType() == PrimitiveType.DEDUCTION || step.primitiveType() == PrimitiveType.EXEMPTION)
                .flatMap(step -> step.impacts().stream().map(impact -> toRow(step, impact)))
                .toList();
        return Map.of("items", deductions);
    }

    private Map<String, Object> toRow(TaxComputation step, TaxImpact impact) {
        return Map.of(
                "pluginId", step.pluginId(),
                "section", step.sectionReference(),
                "primitiveType", step.primitiveType().name(),
                "impactType", impact.taxImpactType().name(),
                "amount", impact.amount(),
                "description", step.explanation()
        );
    }
}
