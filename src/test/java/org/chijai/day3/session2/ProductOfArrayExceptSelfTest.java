package org.chijai.day3.session2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ProductOfArrayExceptSelfTest {

    @Test
    void productExceptSelfUsesLeftAndRightProductsWithoutDivision() {
        assertArrayEquals(
                new int[]{24, 12, 8, 6},
                ProductOfArrayExceptSelf.Optimal.productExceptSelf(new int[]{1, 2, 3, 4})
        );
        assertArrayEquals(
                new int[]{0, 0, 9, 0, 0},
                ProductOfArrayExceptSelf.Optimal.productExceptSelf(new int[]{-1, 1, 0, -3, 3})
        );
    }
}
