package com.taxengine.facts.api;

import com.taxengine.facts.domain.model.Fact;
import com.taxengine.facts.service.fact.FactService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/facts")
public class FactController {
    private final FactService factService;

    public FactController(FactService factService) {
        this.factService = factService;
    }

    @GetMapping("/{taxpayerId}")
    public List<Fact> facts(@PathVariable UUID taxpayerId) {
        return factService.getFactsForTaxpayer(taxpayerId);
    }
}
