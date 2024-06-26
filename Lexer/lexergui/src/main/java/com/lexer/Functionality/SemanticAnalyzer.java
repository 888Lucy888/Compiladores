package com.lexer.Functionality;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import com.lexer.ExtraModules.ErrorHandler;
import com.lexer.ExtraModules.SymbolTableItem;

public class SemanticAnalyzer {
    private static Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<>();
    private static Set<String> functions = new HashSet<String>();

    public static Hashtable<String, Vector<SymbolTableItem>> getSymbolTable() {
        return symbolTable;
    }

    public static ErrorHandler errorHandler = new ErrorHandler("SEMANTIC ANALYZER");
    private static final Stack<Token> stack = new Stack<Token>();

    // Possible operations
    public static final int OP_Plus = 0;
    public static final int OP_Minus = 1;
    public static final int OP_Mult = 2;
    public static final int OP_Division = 3;
    public static final int OP_And = 4;
    public static final int OP_Or = 5;
    public static final int OP_Not = 6;
    public static final int OP_Minor = 7;
    public static final int OP_Greater = 8;
    public static final int OP_Equal = 9;
    public static final int OP_LorEqual = 10;
    public static final int OP_GorEqual = 11;
    public static final int OP_Different = 12;
    public static final int OP_Reminder = 13;
    public static final int OP_Assignation = 14;

    // Possible data types
    // TODO add missing data types: eg. octals and binarys
    public static final int INTEGER = 0;
    public static final int FLOAT = 1;
    public static final int CHAR = 2;
    public static final int STRING = 3;
    public static final int BOOLEAN = 4;
    public static final int VOID = 5;
    public static final int ERROR = -1;

    private static int cube[][][] = {
            // SUM 0
            {
                    { INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, STRING, ERROR, ERROR },
                    { ERROR, ERROR, STRING, STRING, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // MINUS 1
            {
                    { INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // MULTIPLICATION 2
            {
                    { INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // DIVISION 3
            {
                    { INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // AND 4
            {
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // OR 5
            {
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // NOT 6
            {
                    { ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR }
            },
            // LESSER THAN 7
            {
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // GREATER THAN 8
            {
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // LESSER OR EQUAL TO 9
            {
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // GREATER OR EQUAL TO 10
            {
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // EQUAL TO 11
            {
                    { BOOLEAN, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, BOOLEAN, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, BOOLEAN, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // DIFFERENT THAN 12
            {
                    { BOOLEAN, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, BOOLEAN, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, BOOLEAN, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, BOOLEAN, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // REMAINDER 13
            {
                    { INTEGER, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            },
            // ASSIGNMENT 14
            {
                    { INTEGER, ERROR, ERROR, ERROR, ERROR, ERROR },
                    { FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR },
                    { CHAR, ERROR, CHAR, ERROR, ERROR, ERROR },
                    { ERROR, ERROR, STRING, STRING, ERROR, ERROR },
                    { BOOLEAN, ERROR, ERROR, ERROR, BOOLEAN, ERROR },
                    { ERROR, ERROR, ERROR, ERROR, ERROR, ERROR }
            }
    };

    public static void AddVariable(String scope, String type, String id, String value) {
        // Variable exists in same scope
        if (CheckVariable(scope, id)) {
            errorHandler.storeError("Error: Variable '" + id + "' is already defined.");
            return;
        }
        Vector<SymbolTableItem> items = symbolTable.get(id);
        // If no variables add variable
        if (items == null) {
            Vector<SymbolTableItem> newItemVector = new Vector<>();
            symbolTable.put(id, newItemVector);
            items = symbolTable.get(id);
        }
        items.add(new SymbolTableItem(type, scope, value));
    }

    public static void AddVariable(String scope, String type, String id) {
        AddVariable(scope, type, id, "");
    }

    public static void AddVariable(String type, String id) {
        AddVariable("global", type, id, "");
    }

    public static boolean CheckVariable(String scope, String id) {
        Vector<SymbolTableItem> items = symbolTable.get(id);
        if (items == null) {
            return false;
        } else {
            for (SymbolTableItem item : items) {
                if (item.getScope() == scope) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean CheckVariable(String id) {
        Vector<SymbolTableItem> items = symbolTable.get(id);
        if (items == null) {
            return false;
        }
        return true;
    }

    public static String getVariableType(String id) {
        Vector<SymbolTableItem> items = symbolTable.get(id);
        if (items == null)
            return "";
        return items.get(0).getType();
    }

    // Check all function calls regardless of signature
    // to see if a function of x name exists
    // The program should then call PARAM 2 to identify
    // the specific function if any
    public static boolean CheckFunction(String id) {
        for (String elem : functions) {
            if (elem.equals(id))
                return true;
        }
        return false;
    }

    public static void addFunction(String id) {
        functions.add(id);
    }

    public static Stack<Token> getStack() {
        return stack;
    }

    public static void clearVariables() {
        symbolTable.clear();
    }

    private static int convertToDT(String dataType) {
        switch (dataType) {
            case "INTEGER":
            case "int":
                return INTEGER;
            case "FLOAT":
            case "float":
                return FLOAT;
            case "BOOLEAN":
            case "boolean":
                return BOOLEAN;
            case "CHAR":
            case "char":
                return CHAR;
            case "STRING":
            case "string":
                return STRING;
            case "VOID":
            case "void":
                return VOID;
        }
        return -1;
    }

    private static String convertDTToString(int dataType) {
        switch (dataType) {
            case INTEGER:
                return "int";
            case FLOAT:
                return "float";
            case BOOLEAN:
                return "boolean";
            case CHAR:
                return "char";
            case STRING:
                return "string";
            case VOID:
                return "void";
            case ERROR:
                return "error";
        }
        return "error";
    }

    private static int checkOperationB(String type1, String type2, int op) {
        int t1 = convertToDT(type1), t2 = convertToDT(type2);
        if (t1 == -1 || t2 == -1) {
            System.err.println("Invalid type conversion: " + type1 + " -> " + t1 + ", " + type2 + " -> " + t2);
            return ERROR;
        }
        if (op < OP_Plus || op > OP_Assignation) {
            System.err.println("Invalid operation code: " + op);
            return ERROR;
        }
        try {
            return cube[op][t2][t1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Array index out of bounds: op=" + op + ", t1=" + t1 + ", t2=" + t2);
            throw e;
        }
    }

    public static String checkOperationBinary(String type1, String type2, int op) {
        return convertDTToString(checkOperationB(type1, type2, op));
    }

}
