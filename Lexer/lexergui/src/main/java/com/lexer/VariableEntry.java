package com.lexer;

import javafx.beans.property.SimpleStringProperty;

public class VariableEntry {
    private final SimpleStringProperty idValue;
    private final SimpleStringProperty scopeValue;
    private final SimpleStringProperty typeValue;
    private final SimpleStringProperty valueValue;

    public VariableEntry(String idValue, String scopeValue, String typeValue, String valueValue) {
        this.idValue = new SimpleStringProperty(idValue);
        this.scopeValue = new SimpleStringProperty(scopeValue);
        this.typeValue = new SimpleStringProperty(typeValue);
        this.valueValue = new SimpleStringProperty(valueValue);
    }

    public String getIdValue() {
        return idValue.get();
    }

    public String getScopeValue() {
        return scopeValue.get();
    }

    public String getTypeValue() {
        return typeValue.get();
    }

    public String getValueValue() {
        return valueValue.get();
    }
}
