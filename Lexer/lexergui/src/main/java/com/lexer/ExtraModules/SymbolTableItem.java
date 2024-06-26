package com.lexer.ExtraModules;

public class SymbolTableItem {
    private String type;
    private String scope;
    private String value;

    public SymbolTableItem(String type, String scope, String value){
        setScope(scope);
        setType(type);
        setValue(value);
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
