package com.lexer.Functionality;

public class Token {
    private String word;
    private String token;
    private int line;
    private int state = -1;

    public Token(String word, String token, int line) {
        setWord(word);
        setToken(token);
        setLine(line);
    }

    public Token(String word, String token, int line, int state) {
        setWord(word);
        setToken(token);
        setLine(line);
        setState(state);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        if (state != -1) return "Word: " + this.word + " Token: " + this.token + ". At line [" + this.line + "] state: " + state;
        return "Word: " + this.word + " Token: " + this.token + ". At line [" + this.line + "]";
    }
}
