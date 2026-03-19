package com.taxengine.facts.copilot.tool;

import com.taxengine.facts.copilot.model.CopilotContext;

public interface CopilotTool {

    String name();

    String description();

    Object execute(CopilotContext context);
}
