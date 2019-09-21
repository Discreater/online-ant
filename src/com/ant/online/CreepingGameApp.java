package com.ant.online;

import com.ant.online.controller.RootLayoutController;
import com.ant.online.model.Ant;
import com.ant.online.model.CreepingGame;
import com.ant.online.model.PlayRoom;
import com.ant.online.model.Stick;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CreepingGameApp extends Application {

    public static final int VELOCITY = 5;
    private Stage primaryStage;
    private BorderPane rootLayout;


    private Stick stick;
    private ObservableList<Ant> ants = FXCollections.observableArrayList();

    private RootLayoutController rootLayoutController;

    public ObservableList<Ant> getAnts() {
        return ants;
    }

    public Stick getStick() {
        return stick;
    }

    public CreepingGameApp() {
        // Add som sample data
        this.stick = new Stick(300);
        ants.add(new Ant(0, VELOCITY));
        ants.add(new Ant(80, VELOCITY));
        ants.add(new Ant(110, VELOCITY));
        ants.add(new Ant(160, VELOCITY));
        ants.add(new Ant(250, VELOCITY));

//        ants.add(new Ant(0, VELOCITY));
//        ants.add(new Ant(1, VELOCITY));
//        ants.add(new Ant(2, VELOCITY));
    }

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
            loader.setLocation(CreepingGameApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            rootLayoutController = loader.getController();
            rootLayoutController.setCreepingGameApp(this);

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

    public void startPlay(){
        PlayRoom playRoom = new PlayRoom(ants, stick);
        while (playRoom.hasNext()) {
            CreepingGame creepingGame = playRoom.next();
            while (!creepingGame.isGameOver()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                try {
                    creepingGame.nextTick();
                    System.out.println("tick " + creepingGame.getTick());
                    rootLayoutController.changeAnts();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private void tick() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
