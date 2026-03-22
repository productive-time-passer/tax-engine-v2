package com.taxengine.facts.service.validation;

import com.taxengine.facts.domain.enums.FactType;
import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.validation.ValidationRule;
import com.taxengine.facts.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HraClaimValidationRule implements ValidationRule {
    private static final BigDecimal LANDLORD_PAN_THRESHOLD = new BigDecimal("100000");

    @Override
    public void validate(CanonicalFactCandidate candidate) {
        if (candidate.factType() != FactType.RENT_PAYMENT_FACT) {
            return;
        }
        String claimType = String.valueOf(candidate.factData().getOrDefault("claimType", "HRA"));
        if (!"HRA".equalsIgnoreCase(claimType)) {
            return;
        }

        boolean hasRentReceipts = flag(candidate, "hasRentReceipts", false);
        boolean hasRentalAgreement = flag(candidate, "hasRentalAgreement", false);
        if (!hasRentReceipts || !hasRentalAgreement) {
            throw new ValidationException("HRA claim requires rent receipts and rental agreement");
        }

        BigDecimal annualRentPaid = amount(candidate, "annualRentPaid", BigDecimal.ZERO);
        if (annualRentPaid.compareTo(LANDLORD_PAN_THRESHOLD) > 0) {
            String landlordPan = String.valueOf(candidate.factData().getOrDefault("landlordPan", ""));
            if (landlordPan.isBlank()) {
                throw new ValidationException("Landlord PAN is required when annual rent exceeds ₹1,00,000");
            }
        }
    }

    private boolean flag(CanonicalFactCandidate candidate, String key, boolean defaultValue) {
        Object raw = candidate.factData().get(key);
        if (raw == null) {
            return defaultValue;
        }
        if (raw instanceof Boolean b) {
            return b;
        }
        return Boolean.parseBoolean(String.valueOf(raw));
    }

    private BigDecimal amount(CanonicalFactCandidate candidate, String key, BigDecimal defaultValue) {
        Object raw = candidate.factData().get(key);
        if (raw == null) {
            return defaultValue;
        }
        if (raw instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return new BigDecimal(String.valueOf(raw));
    }
}
