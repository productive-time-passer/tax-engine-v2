package com.taxengine.facts.copilot.tool;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.facts.copilot.model.CopilotContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AdvisoryTool implements CopilotTool {
    @Override
    public String name() {
        return "advisories";
    }

    @Override
    public String description() {
        return "Returns prioritized advisory opportunities and risks";
    }

    @Override
    public Object execute(CopilotContext context) {
        List<Map<String, Object>> topAdvisories = context.advisories().stream()
                .limit(5)
                .map(this::toRow)
                .toList();
        return Map.of("items", topAdvisories);
    }

    private Map<String, Object> toRow(Advisory advisory) {
        return Map.of(
                "advisoryId", advisory.advisoryId(),
                "title", advisory.title(),
                "description", advisory.description(),
                "priority", advisory.priority().name(),
                "type", advisory.type().name(),
                "recommendedActions", advisory.recommendedActions()
        );
    }
}
