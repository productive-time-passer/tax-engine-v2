package com.taxengine.facts.copilot.context;

import com.taxengine.facts.copilot.model.CopilotContext;
import org.springframework.stereotype.Component;

@Component
public class CopilotContextFetcher {

    private final CopilotDataProvider dataProvider;

    public CopilotContextFetcher(CopilotDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public CopilotContext fetch(String userId, String financialYear) {
        return new CopilotContext(
                dataProvider.getTaxContext(userId, financialYear),
                dataProvider.getTaxResult(userId, financialYear),
                dataProvider.getAdvisories(userId, financialYear)
        );
    }
}
