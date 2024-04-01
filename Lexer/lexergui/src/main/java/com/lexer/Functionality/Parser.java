package com.lexer.Functionality;

import java.util.Vector;
import javafx.scene.control.TreeItem;

public class Parser {
    private static Vector<Token> tokens;
    private static int currentToken = 0;
    private static TreeItem<String> root = new TreeItem<>("PROGRAM");

    private static TreeItem<String> current_level;

    public static void setTokens(Vector<Token> tokens) {
        Parser.tokens = tokens;
    }

    // These two methods are for better memory management
    private static void clearChildren(TreeItem<String> node) {
        while (node.getChildren().size() > 0) {
            clearChildren(node.getChildren().remove(0)); // Remove and clear the first child
        }
    }

    private static void clearTree(TreeItem<String> root) {
        clearChildren(root);
    }

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
                System.err.println("VARTYPE | ID | ()");
                break;
            case 6:
                System.err.println("Invalid Body");
                break;
            case 7:
                System.err.println("Missing =");
                break;
            case 8:
                System.err.println("Missing ID");
                break;
            case 9:
                System.err.println("(");
                break;
            default:
                break;
        }
    }

    public static TreeItem<String> parse() {

        currentToken = 0;

        clearTree(root);

        RULE_PROGRAM();

        return root;
    }

    private static void RULE_PROGRAM() {

        if (tokens.get(currentToken).getWord().equals("{")) {
            root.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else {
            error(1);
        }

        current_level = root;

        RULE_BODY();

        if (tokens.get(currentToken).getWord().equals("}")) {
            root.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else {
            error(2);
        }
    }

    private static void RULE_BODY() {

        while (!tokens.get(currentToken).getWord().equals("}")) {

            TreeItem<String> child = new TreeItem<>("BODY");
            current_level.getChildren().add(child);
            current_level = child;

            if (tokens.get(currentToken).getToken().equals("ID")) {
                RULE_ASSIGNMENT();
                if (tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isVarType(tokens.get(currentToken).getToken())) {
                RULE_VARIABLE();
                if (tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (tokens.get(currentToken).getWord().equals("while")) {
                RULE_WHILE();
            } else if (tokens.get(currentToken).getWord().equals("if")) {
                RULE_IF();
            } else if (tokens.get(currentToken).getWord().equals("return")) {
                RULE_RETURN();
                if (tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (tokens.get(currentToken).getWord().equals("print")) {
                RULE_PRINT();
                if (tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else
                error(6);
            currentToken++;
        }
    }

    private static void RULE_ASSIGNMENT() {

        TreeItem<String> child = new TreeItem<>("RULE ASSIGNMENT");
        current_level.getChildren().add(child);
        current_level = child;

        if (tokens.get(currentToken).getToken().equals("ID")) {
            currentToken++;
            if (tokens.get(currentToken).getWord().equals("=")) {
                current_level.getChildren().add(new TreeItem<String>("="));
                currentToken++;
                RULE_EXPRESSION();
            } else {
                error(7);
            }
        }
    }

    private static void RULE_VARIABLE() {

        TreeItem<String> child = new TreeItem<>("RULE VARIABLE");
        current_level.getChildren().add(child);
        current_level = child;

        if (isVarType(tokens.get(currentToken).getToken())) {
            currentToken++;
            if (tokens.get(currentToken).getToken().equals("ID")) {
                currentToken++;
            } else
                error(8);
        }
    }

    private static void RULE_WHILE() {

        TreeItem<String> child = new TreeItem<>("RULE WHILE");
        current_level.getChildren().add(child);
        current_level = child;

        if (tokens.get(currentToken).getWord().equals("while")) {
            current_level.getChildren().add(new TreeItem<String>("while"));
            currentToken++;
            if (tokens.get(currentToken).getWord().equals("(")) {
                current_level.getChildren().add(new TreeItem<String>("("));
                currentToken++;
                RULE_EXPRESSION();
                if (tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                    RULE_PROGRAM();
                } else
                    error(4);
            } else
                error(9);
        }
    }

    private static void RULE_IF() {

        TreeItem<String> child = new TreeItem<>("RULE IF");
        current_level.getChildren().add(child);
        current_level = child;

        if (tokens.get(currentToken).getWord().equals("if")) {
            currentToken++;
            current_level.getChildren().add(new TreeItem<String>("if"));
            if (tokens.get(currentToken).getWord().equals("(")) {
                current_level.getChildren().add(new TreeItem<String>("("));
                currentToken++;
                RULE_EXPRESSION();
                if (tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                    RULE_PROGRAM();
                    if (tokens.get(currentToken).getWord().equals("else")) {
                        current_level.getChildren().add(new TreeItem<String>("else"));
                        currentToken++;
                        RULE_PROGRAM();
                    }
                } else
                    error(4);
            } else
                error(9);
        }
    }

    private static void RULE_RETURN() {

        TreeItem<String> child = new TreeItem<>("RULE RETURN");
        current_level.getChildren().add(child);
        current_level = child;

        if (tokens.get(currentToken).getWord().equals("return")) {
            current_level.getChildren().add(new TreeItem<String>("return"));
            currentToken++;
        }
    }

    private static void RULE_PRINT() {

        TreeItem<String> child = new TreeItem<>("RULE PRINT");
        current_level.getChildren().add(child);
        current_level = child;

        if (tokens.get(currentToken).getWord().equals("print")) {
            current_level.getChildren().add(new TreeItem<String>("print"));
            currentToken++;
            if (tokens.get(currentToken).getWord().equals("(")) {
                current_level.getChildren().add(new TreeItem<String>("("));
                currentToken++;
                RULE_EXPRESSION();
                if (tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                } else
                    error(4);
            } else
                error(9);
        }
    }

    private static void RULE_EXPRESSION() {

        TreeItem<String> child = new TreeItem<>("EXPRESSION");
        current_level.getChildren().add(child);
        current_level = child;

        RULE_X();
        while (tokens.get(currentToken).getWord().equals("|")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            RULE_X();
        }
        current_level = child.getParent();
    }

    private static void RULE_X() {

        TreeItem<String> child = new TreeItem<>("RULE X");
        current_level.getChildren().add(child);
        current_level = child;

        RULE_Y();
        while (tokens.get(currentToken).getWord().equals("&")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            RULE_Y();
        }
        current_level = child.getParent();
    }

    private static void RULE_Y() {

        TreeItem<String> child = new TreeItem<>("RULE Y");
        current_level.getChildren().add(child);
        current_level = child;

        if (tokens.get(currentToken).getWord().equals("!")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        }

        RULE_R();
        current_level = child.getParent();
    }

    private static void RULE_R() {

        TreeItem<String> child = new TreeItem<>("RULE R");
        current_level.getChildren().add(child);
        current_level = child;
        RULE_E();

        while (tokens.get(currentToken).getWord().equals("<")
                | tokens.get(currentToken).getWord().equals(">")
                | tokens.get(currentToken).getWord().equals("<=")
                | tokens.get(currentToken).getWord().equals(">=")
                | tokens.get(currentToken).getWord().equals("==")
                | tokens.get(currentToken).getWord().equals("!=")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            RULE_E();
        }
        current_level = child.getParent();
    }

    private static void RULE_E() {

        TreeItem<String> child = new TreeItem<>("RULE E");
        current_level.getChildren().add(child);
        current_level = child;
        RULE_A();

        while (tokens.get(currentToken).getWord().equals("-")
                | tokens.get(currentToken).getWord().equals("+")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            RULE_A();
        }
        current_level = child.getParent();
    }

    private static void RULE_A() {

        TreeItem<String> child = new TreeItem<>("RULE A");
        current_level.getChildren().add(child);
        current_level = child;
        RULE_B();

        while (tokens.get(currentToken).getWord().equals("/")
                | tokens.get(currentToken).getWord().equals("*")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            RULE_B();
        }
        current_level = child.getParent();
    }

    private static void RULE_B() {

        TreeItem<String> child = new TreeItem<>("RULE B");
        current_level.getChildren().add(child);
        current_level = child;

        if (tokens.get(currentToken).getWord().equals("-")) {
            current_level.getChildren().add(new TreeItem<String>("-"));
            currentToken++;
        }

        RULE_C();

        current_level = child.getParent();
    }

    private static void RULE_C() {

        TreeItem<String> child = new TreeItem<>("RULE C");
        current_level.getChildren().add(child);
        current_level = child;

        if (isVarType(tokens.get(currentToken).getToken())) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else if (tokens.get(currentToken).getToken().equals("ID")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else if (tokens.get(currentToken).getToken().equals("KEYWORD")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else if (tokens.get(currentToken).getWord().equals("(")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            RULE_EXPRESSION();
            if (tokens.get(currentToken).getWord().equals(")")) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
            } else
                error(4);
        } else {
            error(5);
            currentToken++;
        }

        current_level = child.getParent();

    }

    private static boolean isVarType(String token) {
        if (token.equals("INTEGER") ||
                token.equals("FLOAT") ||
                token.equals("OCTAL") ||
                token.equals("BINARY") ||
                token.equals("HEXADECIMAL") ||
                token.equals("CHAR") ||
                token.equals("STRING")) {
            return true;
        }
        return false;

    }

}
