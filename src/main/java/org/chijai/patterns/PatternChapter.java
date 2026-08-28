package org.chijai.patterns;

import java.util.List;

public record PatternChapter(
        String topic,
        String category,
        String subCategory,
        String subPattern,
        String anchorProblem,
        List<String> chapterFlow
) {
    public static PatternChapter of(
            String topic,
            String category,
            String subCategory,
            String subPattern,
            String anchorProblem
    ) {
        return new PatternChapter(
                topic,
                category,
                subCategory,
                subPattern,
                anchorProblem,
                List.of(
                        "PROBLEM",
                        "BASELINE",
                        "RECOGNITION",
                        "INVARIANT",
                        "TRAPS",
                        "FALLBACK",
                        "OPTIMAL",
                        "DEFEND"
                )
        );
    }

    public String primaryHome() {
        return topic + " -> " + category + " -> " + subCategory + " -> " + subPattern + " -> " + anchorProblem;
    }
}
