package com.taxengine.advisory.engine;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.advisory.model.AdvisoryPriority;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class AdvisoryPrioritizer {

    public List<Advisory> prioritize(List<Advisory> advisories) {
        Comparator<Advisory> comparator = Comparator
                .comparing((Advisory advisory) -> priorityRank(advisory.priority()))
                .thenComparing(Advisory::potentialImpact, Comparator.reverseOrder())
                .thenComparing(Advisory::advisoryId);

        return advisories.stream().sorted(comparator).toList();
    }

    private int priorityRank(AdvisoryPriority priority) {
        return switch (priority) {
            case HIGH -> 0;
            case MEDIUM -> 1;
            case LOW -> 2;
        };
    }
}
