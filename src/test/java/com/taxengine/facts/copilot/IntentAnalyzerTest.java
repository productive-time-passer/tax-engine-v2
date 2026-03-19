package com.taxengine.facts.copilot;

import com.taxengine.facts.copilot.intent.IntentAnalyzer;
import com.taxengine.facts.copilot.model.Intent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntentAnalyzerTest {

    private final IntentAnalyzer analyzer = new IntentAnalyzer();

    @Test
    void detectsTaxSavingIntent() {
        assertThat(analyzer.detect("How can I save tax this year?")).isEqualTo(Intent.TAX_SAVING);
    }

    @Test
    void detectsComplianceIntent() {
        assertThat(analyzer.detect("Any itr filing deadline risk?")).isEqualTo(Intent.COMPLIANCE);
    }

    @Test
    void defaultsToGeneralQuery() {
        assertThat(analyzer.detect("Can you explain this?")).isEqualTo(Intent.GENERAL_QUERY);
    }
}
