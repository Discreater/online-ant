package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import com.ant.online.model.Ant;
import com.ant.online.model.CreepingGame;
import com.ant.online.model.PlayRoom;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

public class RootLayoutController {
    public static final double stickLength = 600.0;
    public static final double antWidth = 20.0;
    public static final double offset = 100.0;

    @FXML
    private TextField velocityField;
    @FXML
    private TextField stickLengthField;
    @FXML
    private TextArea antsPositionTA;
    @FXML
    private AnchorPane antsImageLayout;

    @FXML
    private TableView<Ant> currentAntTable;
    @FXML
    private TableColumn<Ant, Integer> currentAntSerialColumn;
    @FXML
    private TableColumn<Ant, Integer> currentAntPositionColumn;
    @FXML
    private TableColumn<Ant, String> currentAntFaceColumn;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private TextField minTimeField;
    @FXML
    private TextField maxTimeField;

    private CreepingGameApp creepingGameApp;
    // Reference to the main application

    private CreepingGame currentCreepingGame = null;
    private PlayRoom currentPlayingRoom = null;

    private Boolean running = false;


    private Image antImageLeft;
    private Image antImageRight;

    @FXML
    private void initialize() {
        antImageLeft = new Image("file:resources/images/ant.png");
        antImageRight = new Image("file:resources/images/ant-reverse.png");
    }

    /**
     * When click the "确认" button
     */
    @FXML
    private void handleCommmit(){
        // TODO: initial whole game by the input data,
    }

    /**
     * When click the "开始" button
     */
    @FXML
    private void handleStart() {
        if(running) return;
        running = true;
        this.currentPlayingRoom = new PlayRoom(creepingGameApp.getAnts(), creepingGameApp.getStick());
        currentCreepingGame = currentPlayingRoom.hasNext() ? currentPlayingRoom.next() : null;
        putAnts();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (handleNextTick()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                running = false;
            }
        });
        thread.start();
        System.out.println("Thread started...");
    }

    /**
     * When click the "下一帧" button or invoked by
     *
     * @return only for the {@link RootLayoutController#handleStart()} to invoke
     */
    @NotNull
    @FXML
    private Boolean handleNextTick() {
        if(!currentPlayingRoom.hasNext()){
            return false;
        } else if (currentCreepingGame == null || currentCreepingGame.isGameOver()) {
            this.currentCreepingGame = currentPlayingRoom.next();
            Platform.runLater(() -> putAnts());
            return true;
        } else if (!currentCreepingGame.isGameOver()) {
            try {
                Thread.sleep(10);
                currentCreepingGame.nextTick();
                Platform.runLater(() -> changeAnts());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return true;
            }
        }
        return  false;
    }

    private ImageView[] antsImages = new ImageView[5];

    public void putAnts() {
        for (int i = 0; i < creepingGameApp.getAnts().size(); i++) {
            antsImages[i] = new ImageView(creepingGameApp.getAnts().get(i).isFaceLeft() ? antImageLeft : antImageRight);
            antsImages[i].setFitWidth(35);
            antsImages[i].setPreserveRatio(true);
            antsImages[i].setSmooth(true);
            AnchorPane.setLeftAnchor(antsImages[i], getTruePosition(creepingGameApp.getAnts().get(i)));
            AnchorPane.setBottomAnchor(antsImages[i], 0.0);
            antsImageLayout.getChildren().add(antsImages[i]);
        }
    }

    public void changeAnts() {
        for (int i = 0; i < creepingGameApp.getAnts().size(); i++) {
            if (!creepingGameApp.getAnts().get(i).isOnline()) {
                antsImageLayout.getChildren().remove(antsImages[i]);
            } else {
                AnchorPane.setLeftAnchor(antsImages[i], getTruePosition(creepingGameApp.getAnts().get(i)));
                antsImages[i].setImage(creepingGameApp.getAnts().get(i).isFaceLeft() ? antImageLeft : antImageRight);
            }
        }
    }

    private double getTruePosition(@NotNull Ant ant) {
        return ant.getPosition() * stickLength / creepingGameApp.getStick().getLength() - antWidth / 2.0 + offset;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setCreepingGameApp(CreepingGameApp creepingGameApp) {
        this.creepingGameApp = creepingGameApp;
    }
}
