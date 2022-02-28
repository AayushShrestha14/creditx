package com.sb.solutions.api.eligibility.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sb.solutions.api.eligibility.question.entity.EligibilityQuestion;
import com.sb.solutions.core.enums.Status;

public class EligibilityUtility {

    public static String convertToMockFormula(String formula) {

        int len = formula.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = formula.charAt(i);
            if (Character.isLetter(c)) {
                sb.append("2.0");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Map<String, String> extractOperands(String expression,
        List<EligibilityQuestion> eligibilityQuestions) {
        final char[] characters = expression.toCharArray();
        final Map<String, String> operands = new HashMap<>();
        for (char ch : characters) {
            if (Character.isLetter(ch)) {
                eligibilityQuestions.stream()
                    .filter(eligibilityQuestion -> eligibilityQuestion.getOperandCharacter()
                        .equals(String.valueOf(ch))
                        && eligibilityQuestion.getStatus() == Status.ACTIVE)
                    .findAny().map(eligibilityQuestion -> operands
                    .put(String.valueOf(ch), String.valueOf(eligibilityQuestion.getId())));
                if (ch == 'I') {
                    operands.put("I", "reserved");
                }
            }
        }
        return operands;
    }
}
