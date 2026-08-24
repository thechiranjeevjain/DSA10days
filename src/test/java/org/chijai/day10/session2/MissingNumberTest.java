package org.chijai.day10.session2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MissingNumberTest {

    private final MissingNumber solution = new MissingNumber();

    @Test
    void solvesOfficialExamples() {
        assertEquals(2, solution.missingNumber(new int[]{3, 0, 1}));
        assertEquals(2, solution.missingNumber(new int[]{0, 1}));
        assertEquals(8, solution.missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
    }

    @Test
    void handlesMissingZeroAndMissingN() {
        assertEquals(0, solution.missingNumber(new int[]{1}));
        assertEquals(1, solution.missingNumber(new int[]{0}));
    }
}
