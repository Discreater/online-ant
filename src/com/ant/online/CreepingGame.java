package com.ant.online;

import com.ant.online.controller.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CreepingGame extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage){
        initStage(primaryStage);

        initRootLayout();

        initScreen();

        initUserPanel();
    }

    /**
     * The primary window, includes title bar, title item.
     *
     * @param primaryStage primaryStage
     */
    private void initStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("online ant");
        this.primaryStage.getIcons().add(new Image("file:resources/images/ant_32.png"));
    }

    /**
     * The root layout, includes menu bar
     */
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CreepingGame.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setCreepingGame(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initScreen() {
        // TODO
    }

    private void initUserPanel() {
        // TODO
    }

    public static void main(String[] args) {
        launch(args);
    }
}
