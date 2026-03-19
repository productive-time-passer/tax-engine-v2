package com.taxengine.orchestrator.publish;

public interface EventPublisher {
    void publish(Object event);
}
