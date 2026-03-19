package com.taxengine.advisory.rule;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.advisory.model.AdvisoryType;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;

import java.util.Optional;

public interface AdvisoryRule {

    String ruleId();

    AdvisoryType type();

    boolean isApplicable(TaxContext context, TaxEngineResult result);

    Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result);
}
