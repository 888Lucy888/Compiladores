package com.lexer;

import javafx.beans.property.SimpleStringProperty;

public class TokenEntry {
    private final SimpleStringProperty wordValue;
    private final SimpleStringProperty tokenValue;
    private final SimpleStringProperty lineValue;

    public TokenEntry(String wordValue, String tokenValue, String lineValue) {
        this.wordValue = new SimpleStringProperty(wordValue);
        this.tokenValue = new SimpleStringProperty(tokenValue);
        this.lineValue = new SimpleStringProperty(lineValue);
    }

    public String getWordValue() {
        return wordValue.get();
    }

    public String getTokenValue() {
        return tokenValue.get();
    }

    public String getLineValue() {
        return lineValue.get();
    }
}
