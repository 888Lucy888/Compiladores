package com.lexer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static PrimaryController primaryController;

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {
        MenuBar menu = creatMenuBar();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();
        primaryController = loader.getController();

        VBox layout = new VBox();
        layout.getChildren().addAll(menu, root);

        scene = new Scene(layout, 1200, 800);
        stage.setScene(scene);
        stage.show();
        primaryStage = stage;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void closeApplication() {
        primaryStage.close();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private MenuBar creatMenuBar() {
        // File operations
        Menu fileMenu = new Menu("File");
        MenuItem newFile = new MenuItem("New File");
        newFile.setOnAction(event -> {
            primaryController.newFile();
        });
        MenuItem openFile = new MenuItem("Open File");
        openFile.setOnAction(event -> {
            primaryController.openFile();
        });
        MenuItem saveFile = new MenuItem("Save File");
        saveFile.setOnAction(event -> {
            primaryController.saveFile();
        });

        fileMenu.getItems().addAll(newFile, openFile, saveFile);
        MenuItem runFile = new MenuItem("Run");
        runFile.setOnAction(event -> {
            primaryController.runFile();
        });
        Menu runMenu = new Menu("Build");
        runMenu.getItems().add(runFile);

        MenuBar bar = new MenuBar();
        bar.getMenus().addAll(fileMenu, runMenu);

        return bar;
    }

    public static void main(String[] args) {
        launch();
    }

}