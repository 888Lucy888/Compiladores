package com.lexer.Functionality;

import com.lexer.ExtraModules.ErrorHandler;
import com.lexer.ExtraModules.SymbolTableItem;

import java.util.Hashtable;
import java.util.Vector;


public class SemanticAnalyzer {
    private static Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<>();
    public static ErrorHandler errorHandler = new ErrorHandler("SEMANTIC ANALYZER");
    private static final Stack stack;
    
    int cube [][][]

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
    public static char CHAR = 3;
    public static String STRING = ;
    public static bool BOOLEAN = ;
    public static void VOID = ;



    cube[OP_Plus][INTEGER][INTERGER] = INTEGER;
    cube[OP_Plus][FLOAT][INTERGER] = FLOAT;
    cube[OP_Plus][INTEGER][FLOAT] = FLOAT;


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
