package com.taxengine.facts.copilot.tool;

import com.taxengine.facts.copilot.model.CopilotContext;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ToolRegistry {

    private final List<CopilotTool> tools;

    public ToolRegistry(List<CopilotTool> tools) {
        this.tools = List.copyOf(tools);
    }

    public Map<String, Object> executeAll(CopilotContext context) {
        Map<String, Object> outputs = new LinkedHashMap<>();
        for (CopilotTool tool : tools) {
            outputs.put(tool.name(), tool.execute(context));
        }
        return outputs;
    }
}
