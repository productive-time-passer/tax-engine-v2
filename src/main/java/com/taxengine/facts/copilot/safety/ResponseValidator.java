package com.taxengine.facts.copilot.safety;

import com.taxengine.facts.copilot.model.CopilotContext;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class ResponseValidator {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\b\\d{2,}(?:[.,]\\d+)?\\b");

    public String validate(String llmResponse, CopilotContext context) {
        String sanitized = llmResponse == null ? "" : llmResponse.trim();
        String lowered = sanitized.toLowerCase(Locale.ROOT);

        if (containsUnsafeClaim(lowered)) {
            throw new IllegalArgumentException("Unsafe response blocked: contains guaranteed outcome or illegal advice");
        }

        if (containsLargeNumbers(sanitized) && context.taxEngineResult().finalTaxPayable() == null) {
            throw new IllegalArgumentException("Unsupported numeric claim blocked due to missing tax result context");
        }

        if (!lowered.contains("not financial") && !lowered.contains("consult")) {
            sanitized = sanitized + "\n\nNote: This guidance is informational and not financial or legal advice. Consult a tax professional before acting.";
        }
        return sanitized;
    }

    private boolean containsUnsafeClaim(String lowered) {
        return lowered.contains("guaranteed")
                || lowered.contains("hide income")
                || lowered.contains("evade tax")
                || lowered.contains("100% sure");
    }

    private boolean containsLargeNumbers(String text) {
        return NUMBER_PATTERN.matcher(text).find();
    }
}
