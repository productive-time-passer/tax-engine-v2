package com.taxengine.facts.copilot.service;

import com.taxengine.facts.copilot.context.CopilotContextFetcher;
import com.taxengine.facts.copilot.conversation.ConversationService;
import com.taxengine.facts.copilot.intent.IntentAnalyzer;
import com.taxengine.facts.copilot.llm.LlmClient;
import com.taxengine.facts.copilot.model.ChatMessageRequest;
import com.taxengine.facts.copilot.model.CopilotContext;
import com.taxengine.facts.copilot.model.CopilotResponse;
import com.taxengine.facts.copilot.model.Intent;
import com.taxengine.facts.copilot.prompt.PromptBuilder;
import com.taxengine.facts.copilot.safety.ResponseValidator;
import com.taxengine.facts.copilot.tool.ToolRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CopilotChatService {

    private final IntentAnalyzer intentAnalyzer;
    private final CopilotContextFetcher contextFetcher;
    private final ToolRegistry toolRegistry;
    private final PromptBuilder promptBuilder;
    private final LlmClient llmClient;
    private final ResponseValidator responseValidator;
    private final ConversationService conversationService;

    public CopilotChatService(
            IntentAnalyzer intentAnalyzer,
            CopilotContextFetcher contextFetcher,
            ToolRegistry toolRegistry,
            PromptBuilder promptBuilder,
            LlmClient llmClient,
            ResponseValidator responseValidator,
            ConversationService conversationService
    ) {
        this.intentAnalyzer = intentAnalyzer;
        this.contextFetcher = contextFetcher;
        this.toolRegistry = toolRegistry;
        this.promptBuilder = promptBuilder;
        this.llmClient = llmClient;
        this.responseValidator = responseValidator;
        this.conversationService = conversationService;
    }

    public CopilotResponse chat(ChatMessageRequest request) {
        Intent intent = intentAnalyzer.detect(request.message());
        CopilotContext context = contextFetcher.fetch(request.userId(), request.financialYear());
        Map<String, Object> toolOutputs = toolRegistry.executeAll(context);
        String prompt = promptBuilder.buildPrompt(
                request.message(),
                context,
                intent,
                conversationService.getHistory(request.userId(), request.financialYear(), 8),
                toolOutputs
        );
        String llmOutput = llmClient.generate(prompt);
        String validated = responseValidator.validate(llmOutput, context);

        conversationService.saveMessage(request.userId(), request.financialYear(), "user", request.message());
        conversationService.saveMessage(request.userId(), request.financialYear(), "assistant", validated);

        return new CopilotResponse(validated, deriveSuggestedActions(context, intent), deriveReferences(context, toolOutputs));
    }

    private List<String> deriveSuggestedActions(CopilotContext context, Intent intent) {
        List<String> actions = new ArrayList<>();
        context.advisories().stream()
                .flatMap(advisory -> advisory.recommendedActions().stream())
                .limit(4)
                .forEach(actions::add);

        if (actions.isEmpty() && intent == Intent.TAX_SAVING) {
            actions.add("Review all deduction-linked investments and upload missing proofs.");
        }
        if (actions.isEmpty() && intent == Intent.COMPLIANCE) {
            actions.add("Review compliance checklist and verify any pending filing tasks.");
        }
        return actions;
    }

    private List<String> deriveReferences(CopilotContext context, Map<String, Object> toolOutputs) {
        List<String> refs = new ArrayList<>();
        context.advisories().stream().map(a -> a.advisoryId()).limit(5).forEach(refs::add);
        refs.addAll(toolOutputs.keySet().stream().map(key -> "tool:" + key).toList());
        return refs;
    }
}
