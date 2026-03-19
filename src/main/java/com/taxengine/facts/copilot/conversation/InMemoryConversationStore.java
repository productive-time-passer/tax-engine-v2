package com.taxengine.facts.copilot.conversation;

import com.taxengine.facts.copilot.model.ConversationMessage;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InMemoryConversationStore implements ConversationStore {

    private final Map<String, List<ConversationMessage>> historyByUserAndYear = new ConcurrentHashMap<>();

    @Override
    public void save(ConversationMessage message) {
        String key = key(message.userId(), message.financialYear());
        historyByUserAndYear.computeIfAbsent(key, ignored -> new CopyOnWriteArrayList<>()).add(message);
    }

    @Override
    public List<ConversationMessage> findHistory(String userId, String financialYear, int limit) {
        return historyByUserAndYear.getOrDefault(key(userId, financialYear), List.of())
                .stream()
                .sorted(Comparator.comparing(ConversationMessage::createdAt))
                .skip(Math.max(0, historyByUserAndYear.getOrDefault(key(userId, financialYear), List.of()).size() - limit))
                .toList();
    }

    private String key(String userId, String financialYear) {
        return userId + "::" + financialYear;
    }
}
