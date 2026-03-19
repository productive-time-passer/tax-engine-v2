package com.taxengine.orchestrator.exception;

public class TransientWorkflowException extends RuntimeException {
    public TransientWorkflowException(String message) {
        super(message);
    }
}
