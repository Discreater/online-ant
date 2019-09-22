package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import com.ant.online.model.Ant;
import com.ant.online.model.CreepingGame;
import com.ant.online.model.PlayRoom;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RootLayoutController {
    public static final double stickLength = 600.0;
    public static final double antWidth = 20.0;
    public static final double offset = 100.0;
    // Reference to the main application
    private CreepingGameApp creepingGameApp;

    private CreepingGame currentCreepingGame = null;
    private PlayRoom currentPlayingRoom = null;

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setCreepingGameApp(CreepingGameApp creepingGameApp) {
        this.creepingGameApp = creepingGameApp;
    }

    @FXML
    private void initialize() {
        antImageLeft = new Image("file:resources/images/ant.png");
        antImageRight = new Image("file:resources/images/ant-reverse.png");
    }

    private Image antImageLeft;
    private Image antImageRight;

    @FXML
    private void handleStart() {
        this.currentPlayingRoom = new PlayRoom(creepingGameApp.getAnts(), creepingGameApp.getStick());
        currentCreepingGame = currentPlayingRoom.hasNext() ? currentPlayingRoom.next() : null;
        putAnts();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    handleNextTick();
                }
            }
        });
        thread.start();
        System.out.println("Thread started...");
    }

    @FXML
    private void handleNextTick() {
        if (currentPlayingRoom.hasNext() && (currentCreepingGame == null || currentCreepingGame.isGameOver())) {
            this.currentCreepingGame = currentPlayingRoom.next();
            Platform.runLater(() -> {
                putAnts();
            });
        } else if (!currentCreepingGame.isGameOver()) {
            try {
                Thread.sleep(10);
                currentCreepingGame.nextTick();
                Platform.runLater(() -> {
                    changeAnts();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private AnchorPane antsImageLayout;

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

}
