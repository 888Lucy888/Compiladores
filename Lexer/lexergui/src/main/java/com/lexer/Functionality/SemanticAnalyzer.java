package com.lexer.Functionality;

import com.lexer.ExtraModules.ErrorHandler;
import com.lexer.ExtraModules.SymbolTableItem;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;


public class SemanticAnalyzer {
    private static Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<>();
    public static ErrorHandler errorHandler = new ErrorHandler("SEMANTIC ANALYZER");
    private static final Stack stack;
    
    int cube [][][];

    public static int OP_Plus = 1;
    public static int OP_Minus = 2;
    public static int OP_Mult = 3;
    public static int OP_Division = 4;
    public static int OP_And = 5;
    public static int OP_Or = 6;
    public static int OP_Not = 7;
    public static int OP_Minor = 8;
    public static int OP_Greater = 9;
    public static int OP_Equal = 10;
    public static int OP_LorEqual = 11;
    public static int OP_GorEqual = 12;
    public static int OP_Different = 13;
    public static int OP_Reminder = 14;
    public static int OP_Assignation = 15;



    public static int INTEGER = 1;
    public static int FLOAT = 2;
    public static int CHAR = 3;
    public static int STRING = 4;
    public static int BOOLEAN = 5;
    public static int VOID = 6;



// Suma
    cube[OP_Plus][INTEGER][INTEGER] = INTEGER;
    cube[OP_Plus][FLOAT][INTEGER] = FLOAT;
    cube[OP_Plus][INTEGER][FLOAT] = FLOAT;
    cube[OP_Plus][FLOAT][FLOAT] = FLOAT;
    cube[OP_Plus][CHAR][CHAR] = CHAR;
    cube[OP_Plus][STRING][STRING] = STRING;

// Resta
    cube[OP_Minus][INTEGER][INTEGER] = INTEGER;
    cube[OP_Minus][FLOAT][INTEGER] = FLOAT;
    cube[OP_Minus][INTEGER][FLOAT] = FLOAT;
    cube[OP_Minus][FLOAT][FLOAT] = FLOAT;
    cube[OP_Minus][CHAR][CHAR] = CHAR;

// Multiplicación
    cube[OP_Mult][INTEGER][INTEGER] = INTEGER;
    cube[OP_Mult][FLOAT][INTEGER] = FLOAT;
    cube[OP_Mult][INTEGER][FLOAT] = FLOAT;
    cube[OP_Mult][FLOAT][FLOAT] = FLOAT;
    cube[OP_Mult][CHAR][CHAR] = CHAR;

// División
    cube[OP_Division][INTEGER][INTEGER] = INTEGER;
    cube[OP_Division][FLOAT][INTEGER] = FLOAT;
    cube[OP_Division][INTEGER][FLOAT] = FLOAT;
    cube[OP_Division][FLOAT][FLOAT] = FLOAT;

// And lógico
    cube[OP_And][BOOLEAN][BOOLEAN] = BOOLEAN;

// Or lógico
    cube[OP_Or][BOOLEAN][BOOLEAN] = BOOLEAN;

// Negación lógica
    cube[OP_Not][BOOLEAN][VOID] = BOOLEAN;

// Menor que
    cube[OP_Minor][INTEGER][INTEGER] = BOOLEAN;
    cube[OP_Minor][FLOAT][INTEGER] = BOOLEAN;
    cube[OP_Minor][INTEGER][FLOAT] = BOOLEAN;
    cube[OP_Minor][FLOAT][FLOAT] = BOOLEAN;
    cube[OP_Minor][CHAR][CHAR] = BOOLEAN;

// Mayor que
    cube[OP_Greater][INTEGER][INTEGER] = BOOLEAN;
    cube[OP_Greater][FLOAT][INTEGER] = BOOLEAN;
    cube[OP_Greater][INTEGER][FLOAT] = BOOLEAN;
    cube[OP_Greater][FLOAT][FLOAT] = BOOLEAN;
    cube[OP_Greater][CHAR][CHAR] = BOOLEAN;

// Igual
    cube[OP_Equal][INTEGER][INTEGER] = BOOLEAN;
    cube[OP_Equal][FLOAT][INTEGER] = BOOLEAN;
    cube[OP_Equal][INTEGER][FLOAT] = BOOLEAN;
    cube[OP_Equal][FLOAT][FLOAT] = BOOLEAN;
    cube[OP_Equal][CHAR][CHAR] = BOOLEAN;
    cube[OP_Equal][BOOLEAN][BOOLEAN] = BOOLEAN;

// Menor o igual que
    cube[OP_LorEqual][INTEGER][INTEGER] = BOOLEAN;
    cube[OP_LorEqual][FLOAT][INTEGER] = BOOLEAN;
    cube[OP_LorEqual][INTEGER][FLOAT] = BOOLEAN;
    cube[OP_LorEqual][FLOAT][FLOAT] = BOOLEAN;
    cube[OP_LorEqual][CHAR][CHAR] = BOOLEAN;

// Mayor o igual que
    cube[OP_GorEqual][INTEGER][INTEGER] = BOOLEAN;
    cube[OP_GorEqual][FLOAT][INTEGER] = BOOLEAN;
    cube[OP_GorEqual][INTEGER][FLOAT] = BOOLEAN;
    cube[OP_GorEqual][FLOAT][FLOAT] = BOOLEAN;
    cube[OP_GorEqual][CHAR][CHAR] = BOOLEAN;

// Diferente
    cube[OP_Different][INTEGER][INTEGER] = BOOLEAN;
    cube[OP_Different][FLOAT][INTEGER] = BOOLEAN;
    cube[OP_Different][INTEGER][FLOAT] = BOOLEAN;
    cube[OP_Different][FLOAT][FLOAT] = BOOLEAN;
    cube[OP_Different][CHAR][CHAR] = BOOLEAN;
    cube[OP_Different][BOOLEAN][BOOLEAN] = BOOLEAN;

// Módulo
    cube[OP_Reminder][INTEGER][INTEGER] = INTEGER;

// Asignación
    cube[OP_Assignation][INTEGER][INTEGER] = INTEGER;
    cube[OP_Assignation][FLOAT][INTEGER] = FLOAT;
    cube[OP_Assignation][INTEGER][FLOAT] = FLOAT;
    cube[OP_Assignation][FLOAT][FLOAT] = FLOAT;
    cube[OP_Assignation][CHAR][CHAR] = CHAR;
    cube[OP_Assignation][STRING][STRING] = STRING;
    cube[OP_Assignation][BOOLEAN][BOOLEAN] = BOOLEAN;
    cube[OP_Assignation][VOID][INTEGER] = INTEGER;
    cube[OP_Assignation][VOID][FLOAT] = FLOAT;
    cube[OP_Assignation][VOID][CHAR] = CHAR;
    cube[OP_Assignation][VOID][STRING] = STRING;
    cube[OP_Assignation][VOID][BOOLEAN] = BOOLEAN;


    private static final Hashtable<String, Vector<SymbolTableItem>> getSymbolTable();

    public static void checkVariable(Srting type, String id)

    public static String isBoolean()

    public static String isTypeMatching()

    public static void stackPush(String type)

    public static String stackPop()

    private String calculateType(String type, String operator)

    private String calculateType(String type1, String type2, String operator)

    private static void error(Gui gui, int error, int line, String info)




    /*public static GlobalObject globalObject = new GlobalObject();

    public static class GlobalObject {
        //propiedades y metodos
        public int currentToken;
        public int property2;

        public GlobalObject() {
            // Constructor de los objetos
        }
    }*/

    public static void CheckVariable(String type, String id){
        Vector<SymbolTableItem> items = symbolTable.get(id);
        if (items == null) {
            // If the identifier doesn't exist, insert it into the symbol table
            SymbolTableItem newItem = new SymbolTableItem(type, "global", "");
            Vector<SymbolTableItem> newItemVector = new Vector<>();
            newItemVector.add(newItem);
            symbolTable.put(id, newItemVector);
        } else {
            // If the identifier already exists, report an error
            errorHandler.storeError("Error: Variable '" + id + "' is already defined.");
        }
    }
}
