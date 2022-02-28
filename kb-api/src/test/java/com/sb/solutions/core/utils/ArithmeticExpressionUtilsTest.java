package com.sb.solutions.core.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ArithmeticExpressionUtilsTest {

    @Test
    public void testParseShouldReturnFinalResultFromTheExpressionString() {
        final String expression = "((12*5)+2)/2";

        double value = ArithmeticExpressionUtils.parseExpression(expression);

        assertThat(value, equalTo(31D));
    }
}
