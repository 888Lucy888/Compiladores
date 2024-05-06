package com.lexer.Functionality;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.lexer.ExtraModules.ErrorHandler;

import javafx.scene.control.TreeItem;

public class Parser {
    private static Vector<Token> tokens;
    private static int currentToken = 0;
    public static ErrorHandler errorHandler = new ErrorHandler("PARSER");

    private static TreeItem<String> root = new TreeItem<>("FILE");
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
        if (isCurrentTokenValid()) {
            String errMsg = "\nLine " + (tokens.get(currentToken).getLine() - 1) + ": expected ";
            switch (type) {
                case 1:
                    errMsg += "{";
                    break;
                case 2:
                    errMsg += "}";
                    break;
                case 3:
                    errMsg += ";";
                    break;
                case 4:
                    errMsg += ")";
                    break;
                case 5:
                    errMsg += "VARTYPE | ID | ()";
                    break;
                case 6:
                    errMsg += "valid Body";
                    break;
                case 7:
                    errMsg += "=";
                    break;
                case 8:
                    errMsg += "ID";
                    break;
                case 9:
                    errMsg += "(";
                    break;
                case 10:
                    errMsg += "while";
                    break;
                case 11:
                    errMsg += "break";
                    break;
                case 12:
                    errMsg += "valid data value";
                    break;
                case 13:
                    errMsg += ":";
                    break;
                default:
                    break;
            }
            errorHandler.storeError(errMsg);
            System.err.println(errMsg);
        }
    }

    private static boolean isCurrentTokenValid() {
        return currentToken >= 0 && currentToken < tokens.size();
    }

    public static TreeItem<String> parse() {
        currentToken = 0;

        clearTree(root);
        current_level = root;
        RULE_PROGRAM();

        return root;
    }

    private static void RULE_PROGRAM() {
        TreeItem<String> child = new TreeItem<>("PROGRAM");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("{");

        Set<String> followSet = new HashSet<>();
        followSet.add("EOF");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("{")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else {
            error(1);
        }

        RULE_BODY();

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("}")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else {
            error(2);
        }
        current_level = child.getParent();
    }

    private static void RULE_BODY() {
        TreeItem<String> child = new TreeItem<>("BODY");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("CALCULATE");

        Set<String> followSet = new HashSet<>();
        followSet.add("}");

        while (isCurrentTokenValid() && !tokens.get(currentToken).getWord().equals("}")) {

            if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
                RULE_ASSIGNMENT();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isCurrentTokenValid() &&
                    (isVarType(tokens.get(currentToken).getWord()) &&
                            tokens.get(currentToken).getToken().equals("KEYWORD"))) {
                // } else if (isCurrentTokenValid() &&
                // isVarType(tokens.get(currentToken).getToken())) {
                RULE_VARIABLE();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("while")) {
                RULE_WHILE();
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("do")){
                RULE_DO_WHILE();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("if")) {
                RULE_IF();
            } else if(isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("switch")){
                RULE_SWITCH_CASE();
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("return")) {
                RULE_RETURN();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("print")) {
                RULE_PRINT();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else
                error(6);
            //currentToken++;
        }
        current_level = child.getParent();
    }

    private static void RULE_ASSIGNMENT() {
        TreeItem<String> child = new TreeItem<>("RULE ASSIGNMENT");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("ID");

        Set<String> followSet = new HashSet<>();
        followSet.add(";");

        if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("=")) {
                current_level.getChildren().add(new TreeItem<String>("="));
                currentToken++;
                RULE_EXPRESSION();
            } else {
                error(7);
            }
        }

        current_level = child.getParent();
    }

    private static void RULE_VARIABLE() {
        TreeItem<String> child = new TreeItem<>("RULE VARIABLE");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("int");
        firstSet.add("float");
        firstSet.add("char");
        firstSet.add("string");
        firstSet.add("bool");

        Set<String> followSet = new HashSet<>();
        followSet.add(";");

        if (isCurrentTokenValid() &&
                (isVarType(tokens.get(currentToken).getWord()) &&
                        tokens.get(currentToken).getToken().equals("KEYWORD"))) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                SemanticAnalyzer.CheckVariable(tokens.get(currentToken-1).getWord(),tokens.get(currentToken).getWord());
                currentToken++;
            } else
                error(8);
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("=")) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
                RULE_EXPRESSION();
            }
        }
        current_level = child.getParent();
    }

    private static void RULE_WHILE() {
        TreeItem<String> child = new TreeItem<>("RULE WHILE");
        current_level.getChildren().add(child);
        current_level = child;
        
        Set<String> firstSet = new HashSet<>();
        firstSet.add("while");

        Set<String> followSet = new HashSet<>();
        followSet.add("CALCULATE");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("while")) {
            current_level.getChildren().add(new TreeItem<String>("while"));
            currentToken++;
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                current_level.getChildren().add(new TreeItem<String>("("));
                currentToken++;
                RULE_EXPRESSION();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                    RULE_PROGRAM();
                } else
                    error(4);
            } else
                error(9);
        }
        current_level = child.getParent();
    }

    private static void RULE_DO_WHILE() {
        TreeItem<String> child = new TreeItem<String>("RULE DO WHILE");
        current_level.getChildren().add(child);
        current_level = child;
        
        Set<String> firstSet = new HashSet<>();
        firstSet.add("do");

        Set<String> followSet = new HashSet<>();
        followSet.add(")???");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("do")) {
            current_level.getChildren().add(new TreeItem<String>("do"));
            currentToken++;
            RULE_PROGRAM();
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("while")){
                current_level.getChildren().add(new TreeItem<String>("while"));
                currentToken++;
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                    current_level.getChildren().add(new TreeItem<String>("("));
                    currentToken++;
                    RULE_EXPRESSION();
                    if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
                        current_level.getChildren().add(new TreeItem<String>(")"));
                        currentToken++;
                    } else
                        error(4);
                } else
                    error(9);
            } else
                error(10);
        }
        current_level = child.getParent();
    }

    private static void RULE_IF() {
        TreeItem<String> child = new TreeItem<>("RULE IF");
        current_level.getChildren().add(child);
        current_level = child;
        
        Set<String> firstSet = new HashSet<>();
        firstSet.add("if");

        Set<String> followSet = new HashSet<>();
        followSet.add("}CALCULATE");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("if")) {
            currentToken++;
            current_level.getChildren().add(new TreeItem<String>("if"));
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                current_level.getChildren().add(new TreeItem<String>("("));
                currentToken++;
                RULE_EXPRESSION();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                    RULE_PROGRAM();
                    if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("else")) {
                        current_level.getChildren().add(new TreeItem<String>("else"));
                        currentToken++;
                        RULE_PROGRAM();
                    }
                } else
                    error(4);
            } else
                error(9);
        }
        current_level = child.getParent();
    }

    private static void RULE_SWITCH_CASE() {
        TreeItem<String> child = new TreeItem<>("RULE SWITCH CASE");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("switch");

        Set<String> followSet = new HashSet<>();
        followSet.add("}CALCULATE");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("switch")) {
            current_level.getChildren().add(new TreeItem<String>("switch"));
            currentToken++;
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                current_level.getChildren().add(new TreeItem<String>("("));
                currentToken++;
                RULE_EXPRESSION();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                    if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("{")){
                        current_level.getChildren().add(new TreeItem<String>(";"));
                        currentToken++;
                        while (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("case")) {
                            current_level.getChildren().add(new TreeItem<String>("case"));
                            currentToken++;
                            if (isCurrentTokenValid() && isDataType(tokens.get(currentToken).getToken())){
                                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                                currentToken++;
                                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(":")){
                                    current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                                    currentToken++;
                                    RULE_PROGRAM();
                                    if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("break")) {
                                        current_level.getChildren().add(new TreeItem<String>("break"));
                                        currentToken++;
                                        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                                            current_level.getChildren().add(new TreeItem<String>(";"));
                                            currentToken++;
                                        } else
                                            error(3);
                                    } else
                                        error(11);
                                } else
                                    error(13);
                            } else
                                error(12);
                        };
                        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("}")) {
                            current_level.getChildren().add(new TreeItem<String>("}"));
                            currentToken++;
                        } else
                            error(2);
                    } else
                        error(1);
                } else
                    error(4);
            } else
                error(9);
        }

        current_level = child.getParent();
    }

    private static void RULE_RETURN() {
        TreeItem<String> child = new TreeItem<>("RULE RETURN");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("return");

        Set<String> followSet = new HashSet<>();
        followSet.add(";");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("return")) {
            current_level.getChildren().add(new TreeItem<String>("return"));
            currentToken++;
            RULE_EXPRESSION();
        }
        current_level = child.getParent();
    }

    private static void RULE_PRINT() {
        TreeItem<String> child = new TreeItem<>("RULE PRINT");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("print");

        Set<String> followSet = new HashSet<>();
        followSet.add(";");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("print")) {
            current_level.getChildren().add(new TreeItem<String>("print"));
            currentToken++;
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                current_level.getChildren().add(new TreeItem<String>("("));
                currentToken++;
                RULE_EXPRESSION();
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                } else
                    error(4);
            } else
                error(9);
        }
        current_level = child.getParent();
    }

    private static void RULE_EXPRESSION() {
        TreeItem<String> child = new TreeItem<>("EXPRESSION");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("FIRST(X)");

        Set<String> followSet = new HashSet<>();
        followSet.add(")");
        followSet.add(";");

        RULE_X();
        while (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("|")) {
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

        Set<String> firstSet = new HashSet<>();
        firstSet.add("FIRST(Y)");

        Set<String> followSet = new HashSet<>();
        followSet.add("CALCULATE");

        RULE_Y();
        while (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("&")) {
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
        
        Set<String> firstSet = new HashSet<>();
        firstSet.add("! U FIRST(R)");

        Set<String> followSet = new HashSet<>();
        followSet.add("CALCULATE");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("!")) {
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

        Set<String> firstSet = new HashSet<>();
        firstSet.add("FIRST(E)");

        Set<String> followSet = new HashSet<>();
        followSet.add("FOLLOW(Y)");

        while (isCurrentTokenValid() && (tokens.get(currentToken).getWord().equals("<")
                || tokens.get(currentToken).getWord().equals(">")
                || tokens.get(currentToken).getWord().equals("<=")
                || tokens.get(currentToken).getWord().equals(">=")
                || tokens.get(currentToken).getWord().equals("==")
                || tokens.get(currentToken).getWord().equals("!="))) {
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

        Set<String> firstSet = new HashSet<>();
        firstSet.add("FIRST(A)");

        Set<String> followSet = new HashSet<>();
        followSet.add("CALCULATE");

        while (isCurrentTokenValid() && (tokens.get(currentToken).getWord().equals("-")
                || tokens.get(currentToken).getWord().equals("+"))) {
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

        Set<String> firstSet = new HashSet<>();
        firstSet.add("FIRST(B)");

        Set<String> followSet = new HashSet<>();
        followSet.add("CALCULATE");

        while (isCurrentTokenValid() && (tokens.get(currentToken).getWord().equals("/")
                || tokens.get(currentToken).getWord().equals("*"))) {
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

        Set<String> firstSet = new HashSet<>();
        firstSet.add("CALCULATE");

        Set<String> followSet = new HashSet<>();
        followSet.add("CALCULATE");

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("-")) {
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
        
        Set<String> firstSet = new HashSet<>();
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("TRUE");
        firstSet.add("FALSE");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");

        Set<String> followSet = new HashSet<>();
        followSet.add("FOLLOW(B)");

        if (isCurrentTokenValid() && isDataType(tokens.get(currentToken).getToken())) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("KEYWORD")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            RULE_EXPRESSION();
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
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

    private static boolean isVarType(String token_word) {
        if (token_word.equals("int") ||
                token_word.equals("float") ||
                token_word.equals("char") ||
                token_word.equals("string") ||
                token_word.equals("bool"))
            return true;
        return false;
    }

    private static boolean isDataType(String token) {
        if (token.equals("INTEGER") || token.equals("FLOAT") || token.equals("CHAR") ||
            token.equals("STRING") || token.equals("HEXADECIMAL") || token.equals("OCTAL") ||
            token.equals("BINARY") || token.equals("BOOLEAN"))
            return true;
        return false;
    }

}