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
                case 14:
                    errMsg += "function parameter error";
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
        SemanticAnalyzer.clearVariables();
        current_level = root;
        RULE_PROGRAM("global");

        return root;
    }

    private static void RULE_PROGRAM(String scope) {
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

        RULE_BODY(scope);

        if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("}")) {
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
        } else {
            error(2);
        }
        current_level = child.getParent();
    }

    private static void RULE_BODY(String scope) {
        TreeItem<String> child = new TreeItem<>("BODY");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("CALCULATE");

        Set<String> followSet = new HashSet<>();
        followSet.add("}");

        while (isCurrentTokenValid() && !tokens.get(currentToken).getWord().equals("}")) {

            if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
                RULE_ASSIGNMENT(scope);
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
                RULE_VARIABLE(scope);
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("while")) {
                RULE_WHILE(scope);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("do")){
                RULE_DO_WHILE(scope);
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("if")) {
                RULE_IF(scope);
            } else if(isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("switch")){
                RULE_SWITCH_CASE(scope);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("return")) {
                RULE_RETURN(scope);
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(";")) {
                    current_level.getChildren().add(new TreeItem<String>(";"));
                    currentToken++;
                } else
                    error(3);
            } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("print")) {
                RULE_PRINT(scope);
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

    private static void RULE_ASSIGNMENT(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE ASSIGNMENT");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        firstSet.add("ID");

        Set<String> followSet = new HashSet<>();
        followSet.add(";");

        if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
            if (!SemanticAnalyzer.CheckVariable(tokens.get(currentToken).getWord())){
                SemanticAnalyzer.errorHandler.storeError("Variable " + tokens.get(currentToken).getWord() + " is not defined");
            }
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("=")) {
                current_level.getChildren().add(new TreeItem<String>("="));
                currentToken++;
                RULE_EXPRESSION(scope);
            } else {
                error(7);
            }
        }

        current_level = child.getParent();
    }

    private static void RULE_VARIABLE(String scope) {
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
                currentToken++;
            } else
                error(8);
            // Rule for a function
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                RULE_FUNCTION();
            }
            // Then it is a variable
            else {
                SemanticAnalyzer.AddVariable(scope, tokens.get(currentToken-2).getWord(), tokens.get(currentToken-1).getWord());
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("=")) {
                    current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                    currentToken++;
                    RULE_EXPRESSION(scope);
                }
            }
        }
        current_level = child.getParent();
    }

    private static void RULE_FUNCTION() {
        String type = tokens.get(currentToken - 2).getWord();
        TreeItem<String> child = new TreeItem<>("RULE FUNCTION");
        current_level.getChildren().add(child);
        current_level = child;
        current_level.getChildren().add(new TreeItem<String>("("));
        Set<String> firstSet = new HashSet<>();
        firstSet.add("int");
        Set<String> followSet = new HashSet<>();
        followSet.add("}");
        String signature = tokens.get(currentToken - 1).getWord();
        currentToken++;
        signature = RULE_PARAM_1(signature);
        SemanticAnalyzer.AddVariable("function", type, signature);
        RULE_PROGRAM(signature);
        current_level = child.getParent();
    }

    private static String RULE_PARAM_1(String signature){
        TreeItem<String> child = new TreeItem<>("RULE PARAM 1");
        current_level.getChildren().add(child);
        current_level = child;
        Set<String> firstSet = new HashSet<>();
        firstSet.add("int");
        firstSet.add("float");
        firstSet.add("char");
        firstSet.add("string");
        firstSet.add("bool");
        Set<String> followSet = new HashSet<>();
        followSet.add(")");
        boolean error = false;
        while (isCurrentTokenValid() && (isVarType(tokens.get(currentToken).getWord()) && tokens.get(currentToken).getToken().equals("KEYWORD"))) {
            signature += "_" + tokens.get(currentToken).getWord();
            current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
            currentToken++;
            if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(",")){
                    current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                    currentToken++;
                }
            } else {
                error = true;
                break;
            }
        }
        if (isCurrentTokenValid() && !tokens.get(currentToken).getWord().equals(")"))
            error = true;
        if (error) {
            error(14);
            while(error) {
                for (String value: followSet) {
                    if (tokens.get(currentToken).getWord().equals(value))
                        error = false;
                }
                currentToken++;
            }
        }
        if (tokens.get(currentToken).getWord().equals(")")) currentToken++;
        else error(4);
        current_level.getChildren().add(new TreeItem<>(")"));
        current_level = child.getParent();
        return signature;
    }

    private static void RULE_WHILE(String scope) {
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
                RULE_EXPRESSION(scope);
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                    RULE_PROGRAM(scope);
                } else
                    error(4);
            } else
                error(9);
        }
        current_level = child.getParent();
    }

    private static void RULE_DO_WHILE(String scope) {
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
            RULE_PROGRAM(scope);
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("while")){
                current_level.getChildren().add(new TreeItem<String>("while"));
                currentToken++;
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                    current_level.getChildren().add(new TreeItem<String>("("));
                    currentToken++;
                    RULE_EXPRESSION(scope);
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

    private static void RULE_IF(String scope) {
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
                RULE_EXPRESSION(scope);
                if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
                    current_level.getChildren().add(new TreeItem<String>(")"));
                    currentToken++;
                    RULE_PROGRAM(scope);
                    if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("else")) {
                        current_level.getChildren().add(new TreeItem<String>("else"));
                        currentToken++;
                        RULE_PROGRAM(scope);
                    }
                } else
                    error(4);
            } else
                error(9);
        }
        current_level = child.getParent();
    }
    

    private static void RULE_SWITCH_CASE(String scope) {
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
                RULE_EXPRESSION(scope);
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
                                    RULE_PROGRAM(scope);
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

    private static void RULE_RETURN(String scope) {
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
            RULE_EXPRESSION(scope);
        }
        current_level = child.getParent();
    }

    private static void RULE_PRINT(String scope) {
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
                RULE_EXPRESSION(scope);
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

    private static void RULE_EXPRESSION(String scope) {
        TreeItem<String> child = new TreeItem<>("EXPRESSION");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        // Y FIRST SET
        firstSet.add("!");
        // B FIRST SET
        firstSet.add("-");
        // C FIRST SET
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");

        Set<String> followSet = new HashSet<>();
        followSet.add(")");
        followSet.add(";");

        boolean error = true;
        for (String word : firstSet) {
            if (word.equals(tokens.get(currentToken).getWord()) || word.equals(tokens.get(currentToken).getToken())) {
                error = false;
                break;
            }
        }

        if (!error) {
            RULE_X(scope);
            while (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("|")) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
                RULE_X(scope);
            }
        }
        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        System.out.println(currentToken);
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);

        current_level = child.getParent();
    }

    private static void RULE_X(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE X");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        // Y FIRST SET
        firstSet.add("!");
        // B FIRST SET
        firstSet.add("-");
        // C FIRST SET
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");

        Set<String> followSet = new HashSet<>();
        followSet.add("|");
        followSet.add(")");
        followSet.add(";");

        boolean error = true;
        for (String word : firstSet) {
            if (word.equals(tokens.get(currentToken).getWord()) || word.equals(tokens.get(currentToken).getToken())) {
                error = false;
                break;
            }
        }

        if (!error) {
            RULE_Y(scope);
            while (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("&")) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
                RULE_Y(scope);
            }
        }
        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);

        current_level = child.getParent();
    }

    private static void RULE_Y(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE Y");
        current_level.getChildren().add(child);
        current_level = child;
        
        Set<String> firstSet = new HashSet<>();
        firstSet.add("!");
        // B FIRST SET
        firstSet.add("-");
        // C FIRST SET
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");

        Set<String> followSet = new HashSet<>();
        followSet.add("&");
        followSet.add("|");
        followSet.add(")");
        followSet.add(";");

        boolean error = true;
        for (String word : firstSet) {
            if (word.equals(tokens.get(currentToken).getWord()) || word.equals(tokens.get(currentToken).getToken())) {
                error = false;
                break;
            }
        }

        if (!error) {
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("!")) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
            }

            RULE_R(scope);
        }

        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);

        current_level = child.getParent();
    }

    private static void RULE_R(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE R");
        current_level.getChildren().add(child);
        current_level = child;
        
        Set<String> firstSet = new HashSet<>();
        // B FIRST SET
        firstSet.add("-");
        // C FIRST SET
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");
        
        Set<String> followSet = new HashSet<>();
        followSet.add("&");
        followSet.add("|");
        followSet.add(")");
        followSet.add(";");
        
        boolean error = true;
        for (String word : firstSet) {
            if (word.equals(tokens.get(currentToken).getWord()) || word.equals(tokens.get(currentToken).getToken())) {
                error = false;
                break;
            }
        }
        
        if (!error) {

            RULE_E(scope);

            while (isCurrentTokenValid() && (tokens.get(currentToken).getWord().equals("<")
                    || tokens.get(currentToken).getWord().equals(">")
                    || tokens.get(currentToken).getWord().equals("<=")
                    || tokens.get(currentToken).getWord().equals(">=")
                    || tokens.get(currentToken).getWord().equals("==")
                    || tokens.get(currentToken).getWord().equals("!="))) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
                RULE_E(scope);
            }
        }

        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);

        current_level = child.getParent();
    }

    private static void RULE_E(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE E");
        current_level.getChildren().add(child);
        current_level = child;
        
        Set<String> firstSet = new HashSet<>();
        // B FIRST SET
        firstSet.add("-");
        // C FIRST SET
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");
        
        
        Set<String> followSet = new HashSet<>();
        followSet.add("!=");
        followSet.add("==");
        followSet.add("<");
        followSet.add(">");
        followSet.add("&");
        followSet.add("|");
        followSet.add(")");
        followSet.add(";");
        
        boolean error = true;
        for (String word : firstSet) {
            if (word.equals(tokens.get(currentToken).getWord()) || word.equals(tokens.get(currentToken).getToken())) {
                error = false;
                break;
            }
        }
        
        if (!error) {
            RULE_A(scope);
            while (isCurrentTokenValid() && (tokens.get(currentToken).getWord().equals("-")
                    || tokens.get(currentToken).getWord().equals("+"))) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
                RULE_A(scope);
            }
        }

        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);

        current_level = child.getParent();
    }

    private static void RULE_A(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE A");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        // B FIRST SET
        firstSet.add("-");
        // C FIRST SET
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");

        
        Set<String> followSet = new HashSet<>();
        followSet.add("+");
        followSet.add("-");
        followSet.add("!=");
        followSet.add("==");
        followSet.add("<");
        followSet.add(">");
        followSet.add("&");
        followSet.add("|");
        followSet.add(")");
        followSet.add(";");

        boolean error = true;
        for (String word : firstSet) {
            if (word.equals(tokens.get(currentToken).getWord()) || word.equals(tokens.get(currentToken).getToken())) {
                error = false;
                break;
            }
        }

        if (!error) {
            RULE_B(scope);

            while (isCurrentTokenValid() && (tokens.get(currentToken).getWord().equals("/")
                    || tokens.get(currentToken).getWord().equals("*"))) {
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                currentToken++;
                RULE_B(scope);
            }
        }
        
        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);

        current_level = child.getParent();
    }

    private static void RULE_B(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE B");
        current_level.getChildren().add(child);
        current_level = child;

        Set<String> firstSet = new HashSet<>();
        // B FIRST SET
        firstSet.add("-");
        // C FIRST SET
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");

        Set<String> followSet = new HashSet<>();
        followSet.add("*");
        followSet.add("/");
        followSet.add("+");
        followSet.add("-");
        followSet.add("!=");
        followSet.add("==");
        followSet.add("<");
        followSet.add(">");
        followSet.add("&");
        followSet.add("|");
        followSet.add(")");
        followSet.add(";");

        boolean error = true;
        for (String entry : firstSet) {
            if (entry.equals(tokens.get(currentToken).getWord()) || entry.equals(tokens.get(currentToken).getToken())) {
                error = false;
                if (entry.equals("-")) {
                    current_level.getChildren().add(new TreeItem<String>("-"));
                    currentToken++;
                }
            }
        }

        RULE_C(scope);

        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);

        // if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("-")) {
        //     current_level.getChildren().add(new TreeItem<String>("-"));
        //     currentToken++;
        // }


        

        current_level = child.getParent();
    }

    private static void RULE_C(String scope) {
        TreeItem<String> child = new TreeItem<>("RULE C");
        current_level.getChildren().add(child);
        current_level = child;
        
        Set<String> firstSet = new HashSet<>();
        firstSet.add("INTEGER");
        firstSet.add("OCTAL");
        firstSet.add("HEXADECIMAL");
        firstSet.add("BINARY");
        firstSet.add("STRING");
        firstSet.add("CHAR");
        firstSet.add("FLOAT");
        firstSet.add("ID");
        firstSet.add("true");
        firstSet.add("false");
        firstSet.add("(");

        Set<String> followSet = new HashSet<>();
        followSet.add("*");
        followSet.add("/");
        followSet.add("+");
        followSet.add("-");
        followSet.add("!=");
        followSet.add("==");
        followSet.add("<");
        followSet.add(">");
        followSet.add("&");
        followSet.add("|");
        followSet.add(")");
        followSet.add(";");

        boolean error = true;
        for (String word : firstSet) {
            if (word.equals(tokens.get(currentToken).getWord()) || word.equals(tokens.get(currentToken).getToken())){
                current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
                error = false;
                break;
            }
            if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
                currentToken++;
                RULE_EXPRESSION(scope);
            }
        }

        if (error) {
            error(5);
            while(error) {
                for (String word : followSet) {
                    if (word.equals(tokens.get(currentToken).getWord())){
                        error = false;
                        break;
                    }
                }
                currentToken++;
            }
        } else {
            currentToken++;
            error = true;
            for (String word : followSet) {
                if (word.equals(tokens.get(currentToken).getWord())){
                    error = false;
                    break;
                }
            }
        }
        if (error) error(3);
        

        // if (isCurrentTokenValid() && isDataType(tokens.get(currentToken).getToken())) {
        //     current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
        //     currentToken++;
        // } else if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("ID")) {
        //     current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
        //     currentToken++;
        // } else if (isCurrentTokenValid() && tokens.get(currentToken).getToken().equals("KEYWORD")) {
        //     current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
        //     currentToken++;
        // } else if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals("(")) {
        //     current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
        //     currentToken++;
        //     RULE_EXPRESSION(scope);
        //     if (isCurrentTokenValid() && tokens.get(currentToken).getWord().equals(")")) {
        //         current_level.getChildren().add(new TreeItem<String>(tokens.get(currentToken).getWord()));
        //         currentToken++;
        //     } else
        //         error(4);
        // } else {
        //     error(5);
        //     currentToken++;
        // }

        current_level = child.getParent();

    }

    private static boolean isVarType(String token_word) {
        if (token_word.equals("int") ||
                token_word.equals("float") ||
                token_word.equals("char") ||
                token_word.equals("void") ||
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