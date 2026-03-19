package com.taxengine.engine.context.api;

import com.taxengine.engine.context.builder.TaxContextBuilder;
import com.taxengine.engine.context.domain.TaxContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tax-context")
public class TaxContextController {

    private final TaxContextBuilder taxContextBuilder;

    public TaxContextController(TaxContextBuilder taxContextBuilder) {
        this.taxContextBuilder = taxContextBuilder;
    }

    @GetMapping("/{taxpayerId}/{financialYear}")
    public TaxContext getTaxContext(
            @PathVariable UUID taxpayerId,
            @PathVariable String financialYear
    ) {
        return taxContextBuilder.build(taxpayerId, financialYear);
    }
}
