package com.taxengine.facts.service.extraction;

import com.taxengine.facts.domain.enums.ExtractionMethod;
import com.taxengine.facts.domain.model.RawExtractionRecord;
import com.taxengine.facts.domain.pipeline.DataExtractor;
import com.taxengine.facts.domain.pipeline.PipelineContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class BankStatementExtractor implements DataExtractor {
    @Override
    public boolean supports(String documentType) {
        return "bank-statement".equalsIgnoreCase(documentType);
    }

    @Override
    public List<RawExtractionRecord> extract(PipelineContext context) {
        return List.of(new RawExtractionRecord(
                context.getDocumentMetadata().getTaxpayerId(),
                null,
                "FY2025",
                context.getDocumentMetadata().getId(),
                context.getDocumentMetadata().getDocumentHash(),
                ExtractionMethod.OCR,
                BigDecimal.valueOf(0.82),
                LocalDate.now(),
                Map.of("expense_amount", 4500, "merchant", "Utility", "type", "expense")
        ));
    }
}
