package org.chijai.patterns.dynamicprogramming;

import org.chijai.patterns.PatternChapter;

import java.util.Arrays;

public final class DynamicProgrammingPatternLab {
    private DynamicProgrammingPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Dynamic Programming",
                "Repeated States + Choices",
                "1D State Compression",
                "Take / Skip Or Build Amount",
                "House Robber"
        );
    }

    public static int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }

        int previousTwo = 1;
        int previousOne = 2;
        for (int step = 3; step <= n; step++) {
            int current = previousOne + previousTwo;
            previousTwo = previousOne;
            previousOne = current;
        }
        return previousOne;
    }

    public static int houseRobber(int[] nums) {
        int skip = 0;
        int take = 0;
        for (int value : nums) {
            int nextTake = skip + value;
            int nextSkip = Math.max(skip, take);
            take = nextTake;
            skip = nextSkip;
        }
        return Math.max(skip, take);
    }

    public static int coinChangeMinCoins(int[] coins, int amount) {
        int unreachable = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, unreachable);
        dp[0] = 0;

        for (int current = 1; current <= amount; current++) {
            for (int coin : coins) {
                if (coin <= current) {
                    dp[current] = Math.min(dp[current], dp[current - coin] + 1);
                }
            }
        }

        return dp[amount] == unreachable ? -1 : dp[amount];
    }
}
