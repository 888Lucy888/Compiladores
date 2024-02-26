package com.lexer.Functionality;

import java.util.Vector;

public class Lexer {
    private String text;
    private Vector<Token> tokens;

    private static final String[] KEYWORDS = { "int", "float", "string", "char", "boolean", "void",
            "if", "else", "switch", "case", "while", "do", "for", "true", "false", "print", "return" };

    // CONSTANTS
    private static final int ERROR = 23;
    private static final int STOP = 24;
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

    private static final int[][] stateTable = {
            { 7, 1, 1, 1, ERROR, 15, 15, 15, 15, 15, 15, 15, 15, 21, 16, 19, ERROR, 21, 22, STOP, STOP, STOP }, // InitialState
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
            { 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, ERROR, 17, 17, 17, 17, 17, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, 18,
                    ERROR, ERROR, ERROR, ERROR, ERROR, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 19, 19, 19, 19, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR, ERROR,
                    ERROR, ERROR, STOP, STOP, STOP, STOP, STOP },
            { STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP, STOP,
                    STOP, STOP, STOP, STOP, STOP }
    };

    public Lexer(String text) {
        this.text = text;
    }

    private boolean isDelimiter(char c) {
        char[] delimiters = { ',', ':', ';', '{', '}', '[', ']', '(', ')' };
        for (char delimiter : delimiters) {
            if (c == delimiter)
                return true;
        }
        return false;
    }

    private boolean isOperator(char c) {
        char[] operators = { '+', '-', '*', '/', '<', '>', '=', '!', '&', '|', '%', '?' };
        for (char operator : operators) {
            if (c == operator)
                return true;
        }
        return false;
    }

    private boolean isACD(char currentChar) {
        return ((currentChar == 'a' || currentChar == 'A') || (currentChar == 'c' || currentChar == 'C')
                || (currentChar == 'd' || currentChar == 'D'));
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

    private boolean isG_WYZ(char currentChar) {
        return ((currentChar >= 'g' && currentChar <= 'w') || (currentChar >= 'G' && currentChar <= 'W')
                || currentChar == 'y' || currentChar == 'Y' || currentChar == 'z' || currentChar == 'Z');
    }

    private boolean isX(char currentChar) {
        return currentChar == 'x' || currentChar == 'X';
    }

    private boolean is0(char currentChar) {
        return currentChar == '0';
    }

    private boolean is1(char currentChar) {
        return currentChar == '1';
    }

    private boolean is2_7(char currentChar) {
        return currentChar >= '2' && currentChar <= '7';
    }

    private boolean is8_9(char currentChar) {
        return currentChar == '8' || currentChar == '9';
    }

    private boolean isDollarSign(char currentChar) {
        return currentChar == '$';
    }

    private boolean isUnderscore(char currentChar) {
        return currentChar == '_';
    }

    private boolean isDot(char currentChar) {
        return currentChar == '.';
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

    private boolean isNewLine(char currentChar) {
        return currentChar == '\n';
    }

    private boolean isPlus(char currentChar) {
        return currentChar == '+';
    }

    // Function to check if is a KeyWord
    public static boolean isKeyword(String word) {
        for (String keyword : KEYWORDS) {
            if (keyword.equals(word)) {
                return true;
            }
        }
        return false;
    }

    private void splitLine(int row, String line) {
        int state = 0;
        int index = 0;
        char currentChar;
        if (line.equals(""))
            return;

        int nLine = row;

        String sub = "";
        do {
            currentChar = line.charAt(index);
            if (isWhiteSpace(currentChar) && state == 0) {
                if (isNewLine(currentChar)) {
                    nLine++;
                }
                index++;
                continue;
            }
            sub = sub + currentChar;
            int prevState = state;
            state = calculateNextState(state, currentChar);
            index++;

            if (state == STOP || isNewLine(currentChar)) {
                sub = sub.substring(0, sub.length() - 1);
                tokens.add(checkToken(prevState, sub, nLine));
                sub = "";
                state = 0;
                if (isNewLine(currentChar)) {
                    nLine++;
                }
            }

            if (isDelimiter(currentChar) && (state != 19 && state != 16)) {
                tokens.add(new Token(currentChar + "", "DELIMITER", nLine));
                sub = "";
                state = 0;
            } else if (isOperator(currentChar) && ((state != 19 && state != 16 && state != 5))) {
                tokens.add(new Token(currentChar + "", "OPERATOR", nLine));
                sub = "";
                state = 0;
            }

        } while (index < line.length());

        if (state != 0)
            tokens.add(checkToken(state, sub, nLine));
    }

    private Token checkToken(int state, String word, int row) {
        // STATES
        Token newToken = null;

        if (state == 15) {
            for (String keyword : KEYWORDS) {
                if (word.equals(keyword))
                    state = 25;
            }
        }

        switch (state) {
            case 1:
            case 7:
                newToken = new Token(word, "INTEGER", row);
                break;
            case 2:
            case 3:
            case 6:
            case 14:
                newToken = new Token(word, "FLOAT", row);
                break;
            case 10:
                newToken = new Token(word, "OCTAL", row);
                break;
            case 12:
                newToken = new Token(word, "BINARY", row);
                break;
            case 13:
                newToken = new Token(word, "HEXADECIMAL", row);
                break;
            case 15:
                newToken = new Token(word, "ID", row);
                break;
            case 18:
                newToken = new Token(word, "CHAR", row);
                break;
            case 20:
                newToken = new Token(word, "STRING", row);
                break;
            case 21:
                newToken = new Token(word, "OPERATOR", row);
                break;
            case 22:
                newToken = new Token(word, "DELIMITER", row);
                break;
            case 25:
                newToken = new Token(word, "KEYWORD", row);
                break;
            default:
                newToken = new Token(word, "ERROR", row, state);
        }
        return newToken;
    }

    private int calculateNextState(int state, char currentChar) {
        if (isACD(currentChar))
            return stateTable[state][A_C_D];
        else if (isB(currentChar))
            return stateTable[state][B];
        else if (isE(currentChar))
            return stateTable[state][E];
        else if (isF(currentChar))
            return stateTable[state][F];
        else if (isG_WYZ(currentChar))
            return stateTable[state][G_TO_W_Y_Z];
        else if (isX(currentChar))
            return stateTable[state][X];
        else if (is0(currentChar))
            return stateTable[state][ZERO];
        else if (is1(currentChar))
            return stateTable[state][ONE];
        else if (is2_7(currentChar))
            return stateTable[state][TWO_TO_SEVEN];
        else if (is8_9(currentChar))
            return stateTable[state][EIGHT_NINE];
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
        else if (isWhiteSpace(currentChar))
            return stateTable[state][WHITE_SPACE];
        else if (isDot(currentChar))
            return stateTable[state][DOT];
        else if (isOperator(currentChar))
            return stateTable[state][OPERATOR];
        else if (isDelimiter(currentChar))
            return stateTable[state][DELIMITER];
        return stateTable[ERROR][0];
    }

    public void run() {
        tokens = new Vector<Token>();
        String line = text;
        int lineCount = 1;
        splitLine(lineCount, line);
    }

    public Vector<Token> getTokens() {
        return tokens;
    }
}
