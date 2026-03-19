package com.taxengine.engine.context.builder;

import com.taxengine.engine.context.service.FactQueryService;
import com.taxengine.engine.context.service.PersonService;
import com.taxengine.engine.context.service.TaxPeriodService;
import com.taxengine.engine.context.service.TaxpayerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaxContextBuilderConfig {

    @Bean
    public FactFilter factFilter() {
        return new FactFilter();
    }

    @Bean
    public FactGrouper factGrouper() {
        return new FactGrouper();
    }

    @Bean
    public FactIndexBuilder factIndexBuilder(FactGrouper factGrouper) {
        return new FactIndexBuilder(factGrouper);
    }

    @Bean
    public TaxContextBuilder taxContextBuilder(
            FactQueryService factQueryService,
            FactFilter factFilter,
            FactIndexBuilder factIndexBuilder,
            PersonService personService,
            TaxpayerService taxpayerService,
            TaxPeriodService taxPeriodService
    ) {
        return new TaxContextBuilder(
                factQueryService,
                factFilter,
                factIndexBuilder,
                personService,
                taxpayerService,
                taxPeriodService
        );
    }
}
