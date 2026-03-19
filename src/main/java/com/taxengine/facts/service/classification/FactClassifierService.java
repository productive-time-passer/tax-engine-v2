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
        String rawType = String.valueOf(fields.getOrDefault("type", "other_income_fact"));
        String normalized = rawType.trim().toLowerCase();
        return switch (normalized) {
            case "salaryincomefact", "salary_income_fact" -> FactType.SALARY_INCOME_FACT;
            case "businessincomefact", "business_income_fact" -> FactType.BUSINESS_INCOME_FACT;
            case "freelanceincomefact", "freelance_income_fact" -> FactType.FREELANCE_INCOME_FACT;
            case "rentalincomefact", "rental_income_fact" -> FactType.RENTAL_INCOME_FACT;
            case "interestincomefact", "interest_income_fact" -> FactType.INTEREST_INCOME_FACT;
            case "dividendincomefact", "dividend_income_fact" -> FactType.DIVIDEND_INCOME_FACT;
            case "capitalgaintransactionfact", "capital_gain_transaction_fact" -> FactType.CAPITAL_GAIN_TRANSACTION_FACT;
            case "cryptotransactionfact", "crypto_transaction_fact" -> FactType.CRYPTO_TRANSACTION_FACT;
            case "foreignincomefact", "foreign_income_fact" -> FactType.FOREIGN_INCOME_FACT;
            case "otherincomefact", "other_income_fact", "income" -> FactType.OTHER_INCOME_FACT;

            case "rentpaymentfact", "rent_payment_fact" -> FactType.RENT_PAYMENT_FACT;
            case "insurancepremiumfact", "insurance_premium_fact" -> FactType.INSURANCE_PREMIUM_FACT;
            case "medicalexpensefact", "medical_expense_fact" -> FactType.MEDICAL_EXPENSE_FACT;
            case "educationexpensefact", "education_expense_fact" -> FactType.EDUCATION_EXPENSE_FACT;
            case "donationfact", "donation_fact" -> FactType.DONATION_FACT;
            case "homeloaninterestfact", "home_loan_interest_fact" -> FactType.HOME_LOAN_INTEREST_FACT;

            case "providentfundcontributionfact", "provident_fund_contribution_fact" -> FactType.PROVIDENT_FUND_CONTRIBUTION_FACT;
            case "npscontributionfact", "nps_contribution_fact" -> FactType.NPS_CONTRIBUTION_FACT;
            case "elssinvestmentfact", "elss_investment_fact" -> FactType.ELSS_INVESTMENT_FACT;
            case "lifeinsurancepremiumfact", "life_insurance_premium_fact" -> FactType.LIFE_INSURANCE_PREMIUM_FACT;

            case "capitallossfact", "capital_loss_fact" -> FactType.CAPITAL_LOSS_FACT;
            case "businesslossfact", "business_loss_fact" -> FactType.BUSINESS_LOSS_FACT;
            case "losscarryforwardfact", "loss_carry_forward_fact" -> FactType.LOSS_CARRY_FORWARD_FACT;

            case "tdsfact", "tds_fact" -> FactType.TDS_FACT;
            case "advancetaxpaymentfact", "advance_tax_payment_fact" -> FactType.ADVANCE_TAX_PAYMENT_FACT;
            case "selfassessmenttaxfact", "self_assessment_tax_fact" -> FactType.SELF_ASSESSMENT_TAX_FACT;

            case "foreignassetfact", "foreign_asset_fact" -> FactType.FOREIGN_ASSET_FACT;
            case "directorincompanyfact", "director_in_company_fact" -> FactType.DIRECTOR_IN_COMPANY_FACT;
            case "propertyownershipfact", "property_ownership_fact" -> FactType.PROPERTY_OWNERSHIP_FACT;
            default -> FactType.OTHER_INCOME_FACT;
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
