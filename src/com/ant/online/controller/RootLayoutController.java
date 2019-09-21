package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import com.ant.online.model.Ant;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

public class RootLayoutController {
    public static final double stickLength = 600.0;
    public static final double antWidth = 20.0;
    public static final double offset = 100.0;
    // Reference to the main application
    private CreepingGameApp creepingGameApp;

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
        putAnts();
        creepingGameApp.startPlay();
    }

    @FXML
    private AnchorPane antsImageLayout;

    private ImageView[] antsImages = new ImageView[5];

    private void putAnts() {
        for (int i = 0; i < 5; i++) {
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
        for(int i =0; i < 5; i++){
            if (creepingGameApp.getAnts().get(i).isOnline()) {
                antsImageLayout.getChildren().remove(antsImages[i]);
            } else {
                AnchorPane.setLeftAnchor(antsImages[i], getTruePosition(creepingGameApp.getAnts().get(i)));
                antsImages[i].setImage(creepingGameApp.getAnts().get(i).isFaceLeft() ? antImageLeft : antImageRight);
            }
        }
    }

    private double getTruePosition(@NotNull Ant ant){
        return ant.getPosition() * stickLength / creepingGameApp.getStick().getLength() - antWidth / 2.0 + offset;
    }

}
