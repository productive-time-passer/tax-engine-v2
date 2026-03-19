package com.taxengine.orchestrator;

import com.taxengine.orchestrator.model.ExtractedFact;
import com.taxengine.orchestrator.model.WorkflowContext;
import com.taxengine.orchestrator.port.FactExtractionClient;
import com.taxengine.orchestrator.step.FactExtractionStep;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FactExtractionStepTest {

    @Test
    void executesExtractionAndPopulatesFacts() {
        FactExtractionClient client = mock(FactExtractionClient.class);
        FactExtractionStep step = new FactExtractionStep(client);
        WorkflowContext context = new WorkflowContext(UUID.randomUUID(), "2024-25", UUID.randomUUID(), UUID.randomUUID());
        when(client.extract(any(), anyString(), any(), any())).thenReturn(List.of(new ExtractedFact("INCOME", Map.of("amount", 1))));

        step.execute(context);

        assertEquals(1, context.getFacts().size());
        verify(client, times(1)).extract(any(), anyString(), any(), any());
    }
}
