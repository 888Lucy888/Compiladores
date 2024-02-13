package com.lexer;

import javafx.beans.property.SimpleStringProperty;

public class TokenEntry {
    private final SimpleStringProperty wordValue;
    private final SimpleStringProperty tokenValue;

    public TokenEntry(String wordValue, String tokenValue) {
        this.wordValue = new SimpleStringProperty(wordValue);
        this.tokenValue = new SimpleStringProperty(tokenValue);
    }

    public String getWordValue() {
        return wordValue.get();
    }

    public String getTokenValue() {
        return tokenValue.get();
    }
}
