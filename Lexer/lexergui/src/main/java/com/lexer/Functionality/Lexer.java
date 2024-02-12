package com.lexer.Functionality;

import java.util.Vector;

public class Lexer {
    private String text;
    private Vector<Token> tokens;

    private static final String[] KEYWORDS = { "int", "float", "string", "char", "boolean", "void",
            "if", "else", "switch", "case", "while", "do", "for", "true", "false", "print", "return" };

    // CONSTANTS
    private static final int ERROR = -1;
    private static final int OTHER = -2;

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO_TO_SEVEN = 2;
    private static final int EIGHT_NINE = 3;
    private static final int DOT = 4;
    private static final int G_TO_W_Y_Z = 5;
    private static final int A_C_D = 6;
    private static final int B = 7;
    private static final int E = 8;
    private static final int F = 9;
    private static final int X = 10;
    private static final int DOLLAR_SIGN = 11;
    private static final int UNDERSCORE = 12;
    private static final int HYPHEN = 13;
    private static final int SINGLE_QUOTE = 14;
    private static final int DOUBLE_QUOTE = 15;
    private static final int BACKSLASH = 16;
    private static final int OPERATOR = 17;
    private static final int DELIMITER = 18;
    private static final int WHITE_SPACE = 19;
    private static final int NEW_LINE = 20;
    private static final int STOP = 21;

    private static final int[][] stateTable = {
            { 7, 1, 1, 1, ERROR, 15, 15, 15, 15, 15, 15, 15, 15, 21, 16, 20, ERROR, 21, 22, STOP, STOP, STOP }, // InitialState
            { 1, 1, 1, 1, 3, ERROR, ERROR, ERROR, 4, 2, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, STOP, STOP,
                    STOP, STOP, STOP }, // Integer
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP }, // Float
            { 3, 3, 3, 3, ERROR, ERROR, ERROR, ERROR, 4, 2, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, STOP, STOP,
                    STOP, STOP, STOP }, // Float
            { 6, 6, 6, 6, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, 5, ERROR, ERROR, ERROR, STOP,
                    STOP, STOP, STOP, STOP },
            { 6, 6, 6, 6, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    STOP, STOP, STOP, STOP, STOP },
            { 6, 6, 6, 6, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    STOP, STOP, STOP, STOP, STOP },
            { ERROR, 10, 10, ERROR, 3, ERROR, ERROR, 8, ERROR, ERROR, 9, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, STOP,
                    STOP, STOP, STOP, STOP },
            { 12, 12, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, STOP, STOP, STOP, STOP, STOP },
            { 13, 13, 13, 13, ERROR, ERROR, 13, 13, 13, 13, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, STOP, STOP,
                    STOP, STOP, STOP },
            { 10, 10, 10, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, STOP, STOP, STOP, STOP, STOP },
            { 14, 1, 1, 1, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    STOP, STOP, STOP, STOP, STOP },
            { 12, 12, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, STOP, STOP, STOP, STOP, STOP },
            { 13, 13, 13, 13, ERROR, ERROR, 13, 13, 13, 13, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, STOP, STOP,
                    STOP, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, 3, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { 15, 15, 15, 15, ERROR, 15, 15, 15, 15, 15, 15, 15, 15, ERROR, ERROR, ERROR, ERROR, STOP, STOP, STOP, STOP,
                    STOP },
            { 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, ERROR, ERROR, 17, 17, 17, 17, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, 18,
                    ERROR, ERROR, ERROR, ERROR, ERROR, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, ERROR, 20, 19, 19, 19, 19, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
    };

    public Lexer(String text) {
        this.text = text;
    }

    private boolean isDelimiter(char c) {
        char[] delimiters = { ',', ':', ';', '{', '}', '[', ']', '(', ')' };
        for (int x = 0; x < delimiters.length; x++) {
            if (c == delimiters[x])
                return true;
        }
        return false;
    }

    private boolean isOperator(char c) {
        char[] operators = { '+', '-', '*', '/', '<', '>', '=', '!', '&', '|' };
        for (int x = 0; x < operators.length; x++) {
            if (c == operators[x])
                return true;
        }
        return false;
    }

    private boolean isQuotationMark(char c) {
        char[] quote = { '"', '\'' };
        for (int x = 0; x < quote.length; x++) {
            if (c == quote[x])
                return true;
        }
        return false;
    }

    private boolean isB(char currentChar) {
        return currentChar == 'b' || currentChar == 'B';
    }

    private boolean isE(char currentChar) {
        return currentChar == 'e' || currentChar == 'E';
    }

    private boolean isF(char currentChar) {
        return currentChar == 'f' || currentChar == 'F';
    }

    private boolean isX(char currentChar) {
        return currentChar == 'x' || currentChar == 'X';
    }

    private boolean isDollarSign(char currentChar) {
        return currentChar == '$';
    }

    private boolean isUnderscore(char currentChar) {
        return currentChar == '_';
    }

    private boolean isHyphen(char currentChar) {
        return currentChar == '-';
    }

    private boolean isSingleQuote(char currentChar) {
        return currentChar == '\'';
    }

    private boolean isDoubleQuote(char currentChar) {
        return currentChar == '"';
    }

    private boolean isBackslash(char currentChar) {
        return currentChar == '\\';
    }

    private boolean isWhiteSpace(char currentChar) {
        return currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r';
    }

    private void splitLine(int row, String line) {
        int state = 0;
        int index = 0;
        char currentChar;
        String string = "";
        if (line.equals(""))
            return;

        do {
            currentChar = line.charAt(index);
            state = calculateNextState(state, currentChar);
            if (!isDelimiter(currentChar) && !isOperator(currentChar))
                string = string + currentChar;
            index++;
        } while (index < line.length() && state != STOP);

        // STATES
        if (state == 3) {
            tokens.add(new Token(string, "INTEGER", row));
        } else {
            if (!string.equals(" "))
                tokens.add(new Token(string, "ERROR", row));
        }

        if (isDelimiter(currentChar))
            tokens.add(new Token(currentChar + "", "DELIMITER", row));
        else if (isOperator(currentChar))
            tokens.add(new Token(currentChar + "", "OPERATOR", row));

        if (index < line.length())
            splitLine(row, line.substring(index));
    }

    private int calculateNextState(int state, char currentChar) {
        if (isWhiteSpace(currentChar) || isDelimiter(currentChar) ||
                isOperator(currentChar) || isQuotationMark(currentChar))
            return stateTable[state][DELIMITER];
        // Add is digit, is char, etc
        else if (isB(currentChar))
            return stateTable[state][B];
        else if (isE(currentChar))
            return stateTable[state][E];
        else if (isF(currentChar))
            return stateTable[state][F];
        else if (isX(currentChar))
            return stateTable[state][X];
        else if (isDollarSign(currentChar))
            return stateTable[state][DOLLAR_SIGN];
        else if (isUnderscore(currentChar))
            return stateTable[state][UNDERSCORE];
        else if (isHyphen(currentChar))
            return stateTable[state][HYPHEN];
        else if (isSingleQuote(currentChar))
            return stateTable[state][SINGLE_QUOTE];
        else if (isDoubleQuote(currentChar))
            return stateTable[state][DOUBLE_QUOTE];
        else if (isBackslash(currentChar))
            return stateTable[state][BACKSLASH];
        return stateTable[state][OTHER];
    }

    public void run() {
        tokens = new Vector<Token>();
        String line;
        int lineCount = 1;

        do {
            int lineEnd = text.indexOf(System.lineSeparator());
            if (lineEnd >= 0) {
                line = text.substring(0, lineEnd);
                if (text.length() > 0) {
                    text = text.substring(lineEnd + 1);
                }
            } else {
                line = text;
                text = "";
            }
            splitLine(lineCount, line);
            lineCount++;
        } while (!text.equals(""));
    }

    public Vector<Token> getTokens() {
        return tokens;
    }
}
