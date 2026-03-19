package com.taxengine.facts.copilot.conversation;

import com.taxengine.facts.copilot.model.ConversationMessage;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ConversationService {

    private final ConversationStore conversationStore;

    public ConversationService(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    public void saveMessage(String userId, String financialYear, String role, String message) {
        conversationStore.save(new ConversationMessage(userId, financialYear, role, message, Instant.now()));
    }

    public List<ConversationMessage> getHistory(String userId, String financialYear, int limit) {
        return conversationStore.findHistory(userId, financialYear, limit);
    }
}
