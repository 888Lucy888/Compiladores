package com.lexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Vector;

import com.lexer.Functionality.Lexer;
import com.lexer.Functionality.Parser;
import com.lexer.Functionality.Token;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private TextArea raw_code;
    @FXML
    private TextArea terminal;
    @FXML
    private TableView<TokenEntry> output;
    @FXML
    private TreeView<String> treeView;
    private ObservableList<TokenEntry> entries = FXCollections.observableArrayList();

    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("UP Files", "*.up"));
        fileChooser.setTitle("Open File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            raw_code.setText(loadFileContent(selectedFile));
        }
    }

    @FXML
    public void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("UP Files", "*.up"));
        fileChooser.setTitle("Save As");
        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            if (selectedFile.exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Existing File");
                alert.setHeaderText("File already exists");
                alert.setContentText("Do you wish to overwrite existing file?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK)
                    saveToFile(selectedFile);
            } else
                saveToFile(selectedFile);
        }
    }

    @FXML
    public void newFile() {
        raw_code.setText(null);
    }

    private void saveToFile(File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(raw_code.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadFileContent(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @FXML
    public void runFile() {
        String text = raw_code.getText();
        Lexer lexer = new Lexer(text);
        lexer.run();
        Vector<Token> tokens = lexer.getTokens();
        entries.clear();

        int errorCount = 0;

        for (Token token : tokens) {
            entries.add(new TokenEntry(token.getWord(), token.getToken(), token.getLine() + ""));
            
            if ("ERROR".equals(token.getToken())) {
                errorCount++;
            }
        }

        Parser.setTokens(tokens);

        output.setItems(entries);
        treeView.setRoot(Parser.parse());

        terminal.setText("Words Found: " + tokens.size() + "\nErrors Found: " + errorCount + "\nCorrect Rate: " + (float)(tokens.size() - errorCount) / tokens.size() * 100.0 + "%");
    }

}
