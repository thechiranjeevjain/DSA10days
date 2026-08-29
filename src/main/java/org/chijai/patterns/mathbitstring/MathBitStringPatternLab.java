package org.chijai.patterns.mathbitstring;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayList;
import java.util.List;

public final class MathBitStringPatternLab {
    private MathBitStringPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Math / Bit / String",
                "Invariant Over Simulation",
                "Carry / XOR / Border / Sieve",
                "Expose The Hidden Rule",
                "Add Binary"
        );
    }

    public static String addBinary(String a, String b) {
        StringBuilder result = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;
        while (i >= 0 || j >= 0 || carry != 0) {
            int sum = carry;
            if (i >= 0) {
                sum += a.charAt(i--) - '0';
            }
            if (j >= 0) {
                sum += b.charAt(j--) - '0';
            }
            result.append(sum % 2);
            carry = sum / 2;
        }
        return result.reverse().toString();
    }

    public static int missingNumber(int[] nums) {
        int xor = nums.length;
        for (int i = 0; i < nums.length; i++) {
            xor ^= i;
            xor ^= nums[i];
        }
        return xor;
    }

    public static List<Integer> primesBelow(int n) {
        boolean[] composite = new boolean[n];
        for (int value = 2; value * value < n; value++) {
            if (!composite[value]) {
                for (int multiple = value * value; multiple < n; multiple += value) {
                    composite[multiple] = true;
                }
            }
        }
        List<Integer> primes = new ArrayList<>();
        for (int value = 2; value < n; value++) {
            if (!composite[value]) {
                primes.add(value);
            }
        }
        return primes;
    }
}
