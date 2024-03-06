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

    // THese two methods are for better memory management
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
                System.err.println("INT | ID | ()");
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
            RULE_EXPRESSION();

            if (tokens.get(currentToken).getWord().equals(";")) {
                current_level.getChildren().add(new TreeItem<String>(";"));
                currentToken++;
            } else
                error(3);
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

        if (tokens.get(currentToken).getToken().equals("INTEGER")) {
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

}
