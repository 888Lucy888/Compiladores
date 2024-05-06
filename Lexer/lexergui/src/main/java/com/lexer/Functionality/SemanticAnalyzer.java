package com.lexer.Functionality;

import com.lexer.ExtraModules.ErrorHandler;
import com.lexer.ExtraModules.SymbolTableItem;

import java.util.Hashtable;
import java.util.Vector;


public class SemanticAnalyzer {
    private static Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<>();
    public static ErrorHandler errorHandler = new ErrorHandler("SEMANTIC ANALYZER");

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
