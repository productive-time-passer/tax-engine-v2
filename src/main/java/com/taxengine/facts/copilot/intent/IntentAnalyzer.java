package com.taxengine.facts.copilot.intent;

import com.taxengine.facts.copilot.model.Intent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class IntentAnalyzer {

    public Intent detect(String userMessage) {
        String normalized = userMessage == null ? "" : userMessage.toLowerCase(Locale.ROOT);

        if (containsAny(normalized, "save tax", "tax saving", "deduction", "reduce tax", "80c", "nps")) {
            return Intent.TAX_SAVING;
        }
        if (containsAny(normalized, "breakdown", "why", "computed", "taxable income", "tax summary")) {
            return Intent.TAX_BREAKDOWN;
        }
        if (containsAny(normalized, "compliance", "notice", "filing", "itr", "deadline", "penalty")) {
            return Intent.COMPLIANCE;
        }
        if (containsAny(normalized, "invest", "investment", "portfolio", "elss", "ppf")) {
            return Intent.INVESTMENT_ADVICE;
        }
        return Intent.GENERAL_QUERY;
    }

    private boolean containsAny(String value, String... keywords) {
        for (String keyword : keywords) {
            if (value.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
