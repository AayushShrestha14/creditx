package com.sb.solutions.core.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author Sunil Babu Shrestha on 7/8/2019
 */
public final class ArithmeticExpressionUtils {

    private static final ExpressionParser parser = new SpelExpressionParser();

    private ArithmeticExpressionUtils() {
    }

    /**
     * @throws SpelParseException Caller method need to handle this exception,
     * this exception occur if there is no balanced parentheses in an expression
     * for example unbalanced braces
     */
    public static Double parseExpression(String expressionValue) throws SpelParseException {
        return parser.parseExpression(expressionValue).getValue(Double.class);
    }
}
