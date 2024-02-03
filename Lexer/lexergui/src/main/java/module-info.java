module com.lexer {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.lexer to javafx.fxml;
    exports com.lexer;
}
