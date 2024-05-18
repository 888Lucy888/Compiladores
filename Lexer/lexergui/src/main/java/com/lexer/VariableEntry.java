package com.lexer;

import javafx.beans.property.SimpleStringProperty;

public class VariableEntry {
    private final SimpleStringProperty idValue;
    private final SimpleStringProperty scopeValue;
    private final SimpleStringProperty typeValue;
    private final SimpleStringProperty dataValue;

    public VariableEntry(String idValue, String scopeValue, String typeValue, String dataValue) {
        this.idValue = new SimpleStringProperty(idValue);
        this.scopeValue = new SimpleStringProperty(scopeValue);
        this.typeValue = new SimpleStringProperty(typeValue);
        this.dataValue = new SimpleStringProperty(dataValue);
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

    public String getDataValue() {
        return dataValue.get();
    }
}
