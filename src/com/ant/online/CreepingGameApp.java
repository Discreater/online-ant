package com.ant.online;

import com.ant.online.controller.AnimationThread;
import com.ant.online.controller.RootLayoutController;
import com.ant.online.model.Ant;
import com.ant.online.model.CreepingGame;
import com.ant.online.model.PlayRoom;
import com.ant.online.model.Stick;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreepingGameApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private int velocity = 5;
    private Stick stick;
    private ObservableList<Ant> ants = FXCollections.observableArrayList();
    private List<Ant> initAnts = new ArrayList<>();
    private RootLayoutController rootLayoutController;

    private CreepingGame currentCreepingGame = null;
    private PlayRoom currentPlayingRoom = null;
    private Thread animateThread = null;

    public enum State {
        IDLE, STOPPING, PAUSED, PLAYING, CHANGING
    }

    private State appState;

    public State getAppState() {
        return appState;
    }

    public void setAppState(State appState) {
        this.appState = appState;
        appStateChange();
    }

    private void appStateChange() {
        Platform.runLater(() -> this.rootLayoutController.appStateChange());
    }

    public ObservableList<Ant> getAnts() {
        return ants;
    }

    public Stick getStick() {
        return stick;
    }

    public CreepingGameApp() {

    }

    @Override
    public void start(Stage primaryStage) {
        initStage(primaryStage);

        initRootLayout();

        setDefaultInput();

        setAppState(State.IDLE);

        initScreen();

        initUserPanel();
    }

    public void setDefaultInput() {
        if (appState == State.PLAYING) return;
        // Add som sample data
        this.stick = new Stick(300);
        this.initAnts.clear();
        this.initAnts.add(new Ant(0, velocity));
        this.initAnts.add(new Ant(80, velocity));
        this.initAnts.add(new Ant(110, velocity));
        this.initAnts.add(new Ant(160, velocity));
        this.initAnts.add(new Ant(250, velocity));

        List<Integer> aP = new ArrayList<>(initAnts.size());
        for (Ant ant : initAnts) {
            aP.add(ant.getPosition());
        }
        this.rootLayoutController.setInputForm(5, 300, aP);
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


    public List<Ant> getInitAnts() {
        return initAnts;
    }

    public void setInitAnts(List<Ant> initAnts) {
        this.initAnts = initAnts;
    }

    public void startPlay() {
        ants.clear();
        for (int i=0;i<this.initAnts.size();i++) {
            ants.add(new Ant(initAnts.get(i)));
        }
        this.currentPlayingRoom = new PlayRoom(this.getAnts(), this.getStick());
        this.nextGame();
        this.rootLayoutController.putAnts();
        this.animateThread = new AnimationThread(this);
        this.animateThread.start();
        System.out.println("Thread started...");
    }

    public boolean nextGame() {
        if (currentCreepingGame == null || currentCreepingGame.isGameOver()) {
            if (currentPlayingRoom.hasNext()) {
                this.currentCreepingGame = this.currentPlayingRoom.next();
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean nextTick() {
        assert this.appState == State.PLAYING || this.appState == State.PAUSED;
        if (currentCreepingGame == null || currentCreepingGame.isGameOver()) {
            return false;
        }
        try {
            currentCreepingGame.nextTick();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public RootLayoutController getRootLayoutController() {
        return rootLayoutController;
    }

    public CreepingGame getCurrentCreepingGame() {
        return currentCreepingGame;
    }

    public PlayRoom getCurrentPlayingRoom() {
        return currentPlayingRoom;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
