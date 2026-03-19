package com.taxengine.facts.copilot.tool;

import com.taxengine.advisory.model.AdvisoryType;
import com.taxengine.facts.copilot.model.CopilotContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ComplianceTool implements CopilotTool {
    @Override
    public String name() {
        return "compliance";
    }

    @Override
    public String description() {
        return "Extracts compliance advisories and reminders";
    }

    @Override
    public Object execute(CopilotContext context) {
        List<Map<String, Object>> complianceItems = context.advisories().stream()
                .filter(a -> a.type() == AdvisoryType.RISK || a.type() == AdvisoryType.NUDGE)
                .map(a -> Map.of(
                        "advisoryId", a.advisoryId(),
                        "title", a.title(),
                        "description", a.description(),
                        "actions", a.recommendedActions()
                ))
                .toList();
        return Map.of("items", complianceItems);
    }
}
