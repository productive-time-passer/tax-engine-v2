package com.taxengine.facts.service.classification;

import com.taxengine.facts.domain.enums.FactType;
import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.model.RawExtractionRecord;
import com.taxengine.facts.domain.pipeline.FactClassifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FactClassifierService implements FactClassifier {

    @Override
    public CanonicalFactCandidate classify(RawExtractionRecord record) {
        FactType factType = inferType(record.fields());
        BigDecimal amount = extractAmount(record.fields());
        return new CanonicalFactCandidate(
                record.taxpayerId(),
                record.personId(),
                record.financialYear(),
                factType,
                record.fields(),
                record.sourceDocumentId(),
                record.documentHash(),
                record.extractionMethod(),
                record.confidenceScore(),
                amount,
                record.transactionDate()
        );
    }

    private FactType inferType(Map<String, Object> fields) {
        String type = String.valueOf(fields.getOrDefault("type", "unknown")).toLowerCase();
        return switch (type) {
            case "income" -> FactType.INCOME;
            case "expense" -> FactType.EXPENSE;
            case "investment" -> FactType.INVESTMENT;
            case "tax_credit" -> FactType.TAX_CREDIT;
            case "compliance" -> FactType.COMPLIANCE;
            default -> FactType.UNKNOWN;
        };
    }

    private BigDecimal extractAmount(Map<String, Object> fields) {
        Object salary = fields.get("salary_amount");
        Object expense = fields.get("expense_amount");
        Object fallback = fields.get("amount");
        Object selected = salary != null ? salary : (expense != null ? expense : fallback);
        return selected == null ? BigDecimal.ZERO : new BigDecimal(selected.toString());
    }
}
