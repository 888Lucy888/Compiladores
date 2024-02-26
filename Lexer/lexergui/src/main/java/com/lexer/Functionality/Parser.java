package com.lexer.Functionality;

import java.util.Vector;

public class Parser {
    private static Vector<Token> tokens;
    private static int currentToken;

    private static void error(int type) {
        System.err.println("\nERROR: MISSING ");
        switch (type) {
            case 1:
                System.err.println("{");
                break;
            case 2:
                System.err.println("}");
                break;
            case 3:
                System.err.println(";");
                break;
            case 4:
                System.err.println(")");
                break;
            case 5:
                System.err.println("INT | ID | ()");
                break;
            default:
                break;
        }
    }

    public static void RULE_PROGRAM() {

        if (tokens.get(currentToken).getWord().equals("{")) {
            currentToken++;
        } else {
            error(1);
        }

        RULE_BODY();

        if (tokens.get(currentToken).getWord().equals("}")) {
            currentToken++;
        } else {
            error(2);
        }
    }

    public static void RULE_BODY() {

        while (!tokens.get(currentToken).getWord().equals("}")) {

            RULE_EXPRESSION();

            if (tokens.get(currentToken).getWord().equals(";"))
                currentToken++;
            else
                error(3);
        }
    }

    public static void RULE_EXPRESSION() {

        RULE_X();
        while (tokens.get(currentToken).getWord().equals("|")) {
            currentToken++;
            RULE_X();
        }
    }

    public static void RULE_X() {

        RULE_Y();
        while (tokens.get(currentToken).getWord().equals("&")) {
            currentToken++;
            RULE_Y();
        }
    }

    public static void RULE_Y() {

        if (tokens.get(currentToken).getWord().equals("!")) {
            currentToken++;
        }

        RULE_R();
    }

    public static void RULE_R() {

        RULE_E();

        while (tokens.get(currentToken).getWord().equals("<")
                | tokens.get(currentToken).getWord().equals(">")
                | tokens.get(currentToken).getWord().equals("<=")
                | tokens.get(currentToken).getWord().equals(">=")
                | tokens.get(currentToken).getWord().equals("==")
                | tokens.get(currentToken).getWord().equals("!=")) {
            currentToken++;
            RULE_E();
        }
    }

    public static void RULE_E() {

        RULE_A();

        while (tokens.get(currentToken).getWord().equals("-")
                | tokens.get(currentToken).getWord().equals("+")) {
            currentToken++;
            RULE_A();
        }
    }

    public static void RULE_A() {

        RULE_B();

        while (tokens.get(currentToken).getWord().equals("/")
                | tokens.get(currentToken).getWord().equals("*")) {
            currentToken++;
            RULE_B();
        }
    }

    public static void RULE_B() {

        if (tokens.get(currentToken).getWord().equals("-")) {
            currentToken++;
        }

        RULE_C();
    }

    public static void RULE_C() {

        if (tokens.get(currentToken).getToken().equals("INTEGER")) {
            currentToken++;
        } else if (tokens.get(currentToken).getToken().equals("ID")) {
            currentToken++;
        } else if (tokens.get(currentToken).getWord().equals("(")) {
            currentToken++;
            RULE_EXPRESSION();
            if (tokens.get(currentToken).getWord().equals(")")) {
                currentToken++;
            } else
                error(4);
        } else
            error(5);
    }

}
