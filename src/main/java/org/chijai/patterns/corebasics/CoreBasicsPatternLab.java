package org.chijai.patterns.corebasics;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayList;
import java.util.List;

public final class CoreBasicsPatternLab {
    private CoreBasicsPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Basics / Implementation",
                "Simulation With Boundaries",
                "Parse / Matrix / Counters",
                "Make State Explicit",
                "Spiral Matrix"
        );
    }

    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int col = left; col <= right; col++) {
                result.add(matrix[top][col]);
            }
            top++;
            for (int row = top; row <= bottom; row++) {
                result.add(matrix[row][right]);
            }
            right--;
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    result.add(matrix[bottom][col]);
                }
                bottom--;
            }
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    result.add(matrix[row][left]);
                }
                left++;
            }
        }
        return result;
    }

    public static int atoiClamped(String s) {
        int index = 0;
        while (index < s.length() && s.charAt(index) == ' ') {
            index++;
        }
        int sign = 1;
        if (index < s.length() && (s.charAt(index) == '+' || s.charAt(index) == '-')) {
            sign = s.charAt(index++) == '-' ? -1 : 1;
        }
        long value = 0;
        while (index < s.length() && Character.isDigit(s.charAt(index))) {
            value = value * 10 + (s.charAt(index++) - '0');
            long signed = value * sign;
            if (signed > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            if (signed < Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }
        }
        return (int) value * sign;
    }
}
