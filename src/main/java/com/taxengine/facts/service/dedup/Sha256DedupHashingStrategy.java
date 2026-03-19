package com.taxengine.facts.service.dedup;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
public class Sha256DedupHashingStrategy implements DedupHashingStrategy {
    @Override
    public String hash(CanonicalFactCandidate candidate) {
        try {
            String value = String.join("|",
                    candidate.taxpayerId().toString(),
                    candidate.amount().toPlainString(),
                    candidate.transactionDate().toString(),
                    candidate.documentHash()
            );
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash candidate", e);
        }
    }
}
