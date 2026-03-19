package com.taxengine.advisory.api;

import com.taxengine.advisory.engine.AdvisoryEngine;
import com.taxengine.advisory.model.Advisory;
import com.taxengine.advisory.service.TaxStateProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/advisory")
public class AdvisoryController {

    private final TaxStateProvider taxStateProvider;
    private final AdvisoryEngine advisoryEngine;

    public AdvisoryController(TaxStateProvider taxStateProvider, AdvisoryEngine advisoryEngine) {
        this.taxStateProvider = taxStateProvider;
        this.advisoryEngine = advisoryEngine;
    }

    @GetMapping("/{taxpayerId}/{financialYear}")
    @ResponseStatus(HttpStatus.OK)
    public List<Advisory> getAdvisory(@PathVariable UUID taxpayerId, @PathVariable String financialYear) {
        var snapshot = taxStateProvider.get(taxpayerId, financialYear)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No tax snapshot found for taxpayer=" + taxpayerId + " financialYear=" + financialYear));
        return advisoryEngine.generate(snapshot.context(), snapshot.result());
    }
}
