package com.example.myapplication;

import java.util.ArrayList;

public class Calculator {

    private final ArrayList<String> tokens = new ArrayList<>();

    // History
    private final ArrayList<String> history = new ArrayList<>();
    private boolean advancedMode = false;

    // Turn history on/off
    public void setAdvancedMode(boolean on) {
        advancedMode = on;
    }

    // Get history as multiline text
    public String getHistoryText() {
        if (history.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String line : history) {
            sb.append(line).append("\n");
        }
        return sb.toString().trim();
    }

    public void clearHistory() {
        history.clear();
    }

    // Add digit/operator as string: "7", "+", "-", "*", "/", "C"
    public void push(String value) {
        if (value == null) return;

        // Clear current expression (does NOT clear history)
        if (value.equals("C")) {
            tokens.clear();
            return;
        }

        // Digits
        if (isDigit(value)) {
            // If last token is a number, append to make multi-digit numbers
            if (!tokens.isEmpty() && isNumber(tokens.get(tokens.size() - 1))) {
                String last = tokens.get(tokens.size() - 1);
                tokens.set(tokens.size() - 1, last + value);
            } else {
                tokens.add(value);
            }
            return;
        }

        // Operators
        if (isOperator(value)) {
            if (tokens.isEmpty()) return; // can't start with operator
            if (isOperator(tokens.get(tokens.size() - 1))) return; // prevent ++, *-, etc.
            tokens.add(value);
        }
    }

    // Calculate left-to-right (no operator precedence)
    public int calculate() {
        if (tokens.isEmpty()) return 0;
        if (!isNumber(tokens.get(0))) return 0;

        // Save the expression BEFORE we clear tokens
        String expressionBefore = getExpression();

        int result = Integer.parseInt(tokens.get(0));

        for (int i = 1; i < tokens.size() - 1; i += 2) {
            String op = tokens.get(i);
            String nextToken = tokens.get(i + 1);

            if (!isOperator(op) || !isNumber(nextToken)) break;

            int next = Integer.parseInt(nextToken);

            switch (op) {
                case "+": result += next; break;
                case "-": result -= next; break;
                case "*": result *= next; break;
                case "/":
                    if (next == 0) throw new ArithmeticException("Division by zero");
                    result /= next;
                    break;
            }
        }

        // Save to history ONLY if advanced mode is on
        if (advancedMode) {
            history.add(expressionBefore + "=" + result);
        }

        // Reset tokens to result so user can continue
        tokens.clear();
        tokens.add(String.valueOf(result));

        return result;
    }

    public String getExpression() {
        if (tokens.isEmpty()) return "0";
        StringBuilder sb = new StringBuilder();
        for (String t : tokens) sb.append(t);
        return sb.toString();
    }

    private boolean isDigit(String s) {
        return s.length() == 1 && Character.isDigit(s.charAt(0));
    }

    // allows multi-digit numbers like "78"
    private boolean isNumber(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }
}
