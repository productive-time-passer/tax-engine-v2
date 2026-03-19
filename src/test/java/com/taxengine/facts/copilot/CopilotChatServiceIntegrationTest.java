package com.taxengine.facts.copilot;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.facts.copilot.context.CopilotDataProvider;
import com.taxengine.facts.copilot.context.CopilotContextFetcher;
import com.taxengine.facts.copilot.conversation.ConversationService;
import com.taxengine.facts.copilot.conversation.InMemoryConversationStore;
import com.taxengine.facts.copilot.intent.IntentAnalyzer;
import com.taxengine.facts.copilot.llm.LlmClient;
import com.taxengine.facts.copilot.model.ChatMessageRequest;
import com.taxengine.facts.copilot.model.CopilotResponse;
import com.taxengine.facts.copilot.prompt.PromptBuilder;
import com.taxengine.facts.copilot.safety.ResponseValidator;
import com.taxengine.facts.copilot.service.CopilotChatService;
import com.taxengine.facts.copilot.tool.AdvisoryTool;
import com.taxengine.facts.copilot.tool.ComplianceTool;
import com.taxengine.facts.copilot.tool.DeductionBreakdownTool;
import com.taxengine.facts.copilot.tool.TaxSummaryTool;
import com.taxengine.facts.copilot.tool.ToolRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CopilotChatServiceIntegrationTest {

    @Test
    void executesEndToEndChatFlow() {
        CopilotDataProvider dataProvider = new CopilotDataProvider() {
            @Override
            public TaxContext getTaxContext(String userId, String financialYear) {
                return CopilotFixtures.sampleContext().taxContext();
            }

            @Override
            public TaxEngineResult getTaxResult(String userId, String financialYear) {
                return CopilotFixtures.sampleContext().taxEngineResult();
            }

            @Override
            public List<Advisory> getAdvisories(String userId, String financialYear) {
                return CopilotFixtures.sampleContext().advisories();
            }
        };

        CopilotChatService service = new CopilotChatService(
                new IntentAnalyzer(),
                new CopilotContextFetcher(dataProvider),
                new ToolRegistry(List.of(new TaxSummaryTool(), new DeductionBreakdownTool(), new AdvisoryTool(), new ComplianceTool())),
                new PromptBuilder(new com.fasterxml.jackson.databind.ObjectMapper()),
                new FixedLlmClient(),
                new ResponseValidator(),
                new ConversationService(new InMemoryConversationStore())
        );

        CopilotResponse response = service.chat(new ChatMessageRequest("user-1", "2024-25", "How can I save tax?"));

        assertThat(response.answer()).contains("not financial or legal advice");
        assertThat(response.suggestedActions()).isNotEmpty();
        assertThat(response.references()).contains("ADV-80C", "tool:tax_summary", "tool:compliance");
    }

    private static class FixedLlmClient implements LlmClient {

        @Override
        public String provider() {
            return "test";
        }

        @Override
        public String generate(String prompt) {
            return "Use advisory ADV-80C and verify deduction utilization against your tax summary.";
        }
    }
}
