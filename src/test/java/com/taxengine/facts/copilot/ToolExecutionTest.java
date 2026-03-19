package com.taxengine.facts.copilot;

import com.taxengine.facts.copilot.tool.AdvisoryTool;
import com.taxengine.facts.copilot.tool.ComplianceTool;
import com.taxengine.facts.copilot.tool.DeductionBreakdownTool;
import com.taxengine.facts.copilot.tool.TaxSummaryTool;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ToolExecutionTest {

    @Test
    void runsTaxSummaryTool() {
        Object output = new TaxSummaryTool().execute(CopilotFixtures.sampleContext());
        assertThat(output).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) output)).containsKey("finalTaxPayable");
    }

    @Test
    void runsDeductionBreakdownTool() {
        Object output = new DeductionBreakdownTool().execute(CopilotFixtures.sampleContext());
        assertThat(output).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) output)).containsKey("items");
    }

    @Test
    void runsAdvisoryAndComplianceTools() {
        Object advisoryOutput = new AdvisoryTool().execute(CopilotFixtures.sampleContext());
        Object complianceOutput = new ComplianceTool().execute(CopilotFixtures.sampleContext());

        assertThat(((Map<?, ?>) advisoryOutput)).containsKey("items");
        assertThat(((Map<?, ?>) complianceOutput)).containsKey("items");
    }
}
