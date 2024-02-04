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

    @Override
    public void start(Stage stage) throws IOException {
        MenuBar menu = creatMenuBar();
        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));

        VBox layout = new VBox();
        layout.getChildren().addAll(menu, root);

        scene = new Scene(layout, 800, 600);
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
        MenuItem openFile = new MenuItem("Open File");
        MenuItem saveFile = new MenuItem("Save File");

        fileMenu.getItems().addAll(newFile, openFile, saveFile);

        MenuBar bar = new MenuBar();
        bar.getMenus().addAll(fileMenu);

        return bar;
    }

    public static void main(String[] args) {
        launch();
    }

}