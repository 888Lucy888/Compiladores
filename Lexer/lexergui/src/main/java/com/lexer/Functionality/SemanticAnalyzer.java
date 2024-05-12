package com.lexer.Functionality;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import com.lexer.ExtraModules.ErrorHandler;
import com.lexer.ExtraModules.SymbolTableItem;


public class SemanticAnalyzer {
    private static Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<>();
    
    public static Hashtable<String, Vector<SymbolTableItem>> getSymbolTable() {
        return symbolTable;
    }

    public static ErrorHandler errorHandler = new ErrorHandler("SEMANTIC ANALYZER");
    private static final Stack stack = new Stack<>();

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

    private static int cube [][][] = 
    {
        // SUM
        {
            {INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, STRING, ERROR, ERROR},
            {ERROR, ERROR, STRING, STRING, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // MINUS
        {
            {INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // MULTIPLICATION
        {
            {INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // DIVISION
        {
            {INTEGER, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // AND
        {
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // OR
        {
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // NOT
        {
            {ERROR, ERROR, ERROR, ERROR, BOOLEAN, ERROR}
        },
        // LESSER THAN
        {
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // GREATER THAN
        {
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // LESSER OR EQUAL TO
        {
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // GREATER OR EQUAL TO
        {
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {BOOLEAN, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // EQUAL TO
        {
            {BOOLEAN, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, BOOLEAN, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, BOOLEAN, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // DIFFERENT THAN
        {
            {BOOLEAN, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, BOOLEAN, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, BOOLEAN, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, BOOLEAN, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // REMAINDER
        {
            {INTEGER, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        },
        // ASSIGNMENT
        {
            {INTEGER, ERROR, ERROR, ERROR, ERROR, ERROR},
            {FLOAT, FLOAT, ERROR, ERROR, ERROR, ERROR},
            {CHAR, ERROR, CHAR, ERROR, ERROR, ERROR},
            {ERROR, ERROR, STRING, STRING, ERROR, ERROR},
            {BOOLEAN, ERROR, ERROR, ERROR, BOOLEAN, ERROR},
            {ERROR, ERROR, ERROR, ERROR, ERROR, ERROR}
        }
    };

    public static void AddVariable(String scope, String type, String id, String value) {
        // Variable exists in same scope
        if (CheckVariable(scope, type, value, id)) {
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

    public static boolean CheckVariable(String scope, String type, String value, String id){
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

    public static boolean CheckVariable(String scope, String type, String id){
        return CheckVariable(scope, type, "", id);
    }

    public static boolean CheckVariable(String type, String id){
        return CheckVariable("global", type, "", id);
    }

}
