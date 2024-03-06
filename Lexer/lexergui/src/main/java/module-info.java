module com.lexer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.lexer to javafx.fxml;
    exports com.lexer;
}
