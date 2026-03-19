package com.taxengine.facts.copilot.conversation;

import com.taxengine.facts.copilot.model.ConversationMessage;

import java.util.List;

public interface ConversationStore {
    void save(ConversationMessage message);

    List<ConversationMessage> findHistory(String userId, String financialYear, int limit);
}
