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
        return List.of(
                new RawExtractionRecord(
                        context.getDocumentMetadata().getTaxpayerId(),
                        null,
                        "FY2025",
                        context.getDocumentMetadata().getId(),
                        context.getDocumentMetadata().getDocumentHash(),
                        ExtractionMethod.PARSER,
                        BigDecimal.valueOf(0.93),
                        LocalDate.now(),
                        Map.of("salary_amount", 480000, "component", "BASIC_SALARY", "pan", "ABCDE1234F", "type", "salary_income_fact")
                ),
                new RawExtractionRecord(
                        context.getDocumentMetadata().getTaxpayerId(),
                        null,
                        "FY2025",
                        context.getDocumentMetadata().getId(),
                        context.getDocumentMetadata().getDocumentHash(),
                        ExtractionMethod.PARSER,
                        BigDecimal.valueOf(0.90),
                        LocalDate.now(),
                        Map.ofEntries(
                                Map.entry("type", "rent_payment_fact"),
                                Map.entry("claimType", "HRA"),
                                Map.entry("rent_amount", 180000),
                                Map.entry("hra_amount", 240000),
                                Map.entry("actualHraReceived", 240000),
                                Map.entry("basicSalary", 480000),
                                Map.entry("dearnessAllowance", 0),
                                Map.entry("annualRentPaid", 180000),
                                Map.entry("city", "Pune"),
                                Map.entry("isRentedAccommodation", true),
                                Map.entry("ownsResidence", false),
                                Map.entry("hasRentReceipts", true),
                                Map.entry("hasRentalAgreement", true),
                                Map.entry("landlordPan", "ABCDE1234F"),
                                Map.entry("paidToRelationship", "LANDLORD")
                        )
                )
        );
    }
}
