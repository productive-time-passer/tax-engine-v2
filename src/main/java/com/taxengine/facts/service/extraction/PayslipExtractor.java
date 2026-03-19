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
public class PayslipExtractor implements DataExtractor {
    @Override
    public boolean supports(String documentType) {
        return "payslip".equalsIgnoreCase(documentType);
    }

    @Override
    public List<RawExtractionRecord> extract(PipelineContext context) {
        return List.of(new RawExtractionRecord(
                context.getDocumentMetadata().getTaxpayerId(),
                null,
                "FY2025",
                context.getDocumentMetadata().getId(),
                context.getDocumentMetadata().getDocumentHash(),
                ExtractionMethod.PARSER,
                BigDecimal.valueOf(0.93),
                LocalDate.now(),
                Map.of("salary_amount", 120000, "pan", "ABCDE1234F", "type", "income")
        ));
    }
}
