package com.lexer.Functionality;

import java.util.Vector;

public class Lexer {
    private String text;
    private Vector<Token> tokens;

    private static final String[] KEYWORDS = {"int","float","string","char","boolean","void",
        "if","else","switch","case","while","do","for","true","false","print","return"};

    // CONSTANTS
    private static final int DELIMITER = -1;
    private static final int STOP = -2;
    private static final int ERROR = -3;
    private static final int OTHER = -4;


    private static final int[][] stateTable = {
        {ERROR, ERROR, ERROR, ERROR, STOP},
        {ERROR, ERROR, ERROR, ERROR, STOP},
        {ERROR, ERROR, ERROR, ERROR, STOP},
        {ERROR, ERROR, ERROR, ERROR, STOP},
        {ERROR, ERROR, ERROR, ERROR, STOP},
    };

    public Lexer(String text){
        this.text = text;
    }

    private boolean isDelimiter(char c){
        char [] delimiters = {',',':',';','{','}','[',']','(',')'};
        for (int x=0; x<delimiters.length; x++){
            if (c==delimiters[x]) return true;
        }
        return false;
    }

    private boolean isOperator(char c){
        char [] operators = {'+','-','*','/','<','>','=','!','&','|'};
        for (int x=0; x<operators.length; x++){
            if (c==operators[x]) return true;
        }
        return false;
    }

    private boolean isQuotationMark(char c){
        char [] quote = {'"','\''};
        for (int x=0; x<quote.length; x++){
            if (c==quote[x]) return true;
        }
        return false;
    }

    private boolean isSpace(char c){
        return c == ' ';
    }

    private void splitLine(int row, String line){
        int state = 0;
        int index = 0;
        char currentChar;
        String string = "";
        if(line.equals("")) return;

        do{
            currentChar = line.charAt(index);
            state = calculateNextState(state, currentChar);
            if(!isDelimiter(currentChar) && !isOperator(currentChar))
                string = string + currentChar;
            index++;
        } while (index < line.length() && state != STOP);

        // STATES


        if(isDelimiter(currentChar))
            tokens.add(new Token(currentChar+"", "DELIMITER", row));
        else if(isOperator(currentChar))
            tokens.add(new Token(currentChar+"", "OPERATOR", row));

        if (index<line.length())
            splitLine(row, line.substring(index));
    }
    
    private int calculateNextState(int state, char currentChar){
        if (isSpace(currentChar) || isDelimiter(currentChar) || 
            isOperator(currentChar) || isQuotationMark(currentChar))
            return stateTable[state][DELIMITER];
        return stateTable[state][OTHER];
    }

    public void run(){
        tokens = new Vector<Token>();
        String line;
        int lineCount = 1;

        do{
            int lineEnd = text.indexOf(System.lineSeparator());
            if(lineEnd >= 0){
                line = text.substring(0, lineEnd);
                if(text.length()>0){
                    text = text.substring(lineEnd+1);
                }
            }else{
                line = text;
                text = "";
            }
            splitLine (lineCount, line);
            lineCount++;
        } while (!text.equals(""));
    }

    public Vector<Token> getTokens(){
        return tokens;
    }
}
