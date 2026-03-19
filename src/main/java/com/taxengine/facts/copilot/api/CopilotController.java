package com.taxengine.facts.copilot.api;

import com.taxengine.facts.copilot.model.ChatMessageRequest;
import com.taxengine.facts.copilot.model.CopilotResponse;
import com.taxengine.facts.copilot.service.CopilotChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/copilot")
public class CopilotController {

    private final CopilotChatService copilotChatService;

    public CopilotController(CopilotChatService copilotChatService) {
        this.copilotChatService = copilotChatService;
    }

    @PostMapping("/chat")
    public CopilotResponse chat(@Valid @RequestBody ChatMessageRequest request) {
        return copilotChatService.chat(request);
    }
}
