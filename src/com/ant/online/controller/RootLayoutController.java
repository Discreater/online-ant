package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class RootLayoutController {
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
        // TODO
    }

    private Image antImageLeft;
    private Image antImageRight;

    @FXML
    private void handleStart() {
        for (int i = 0; i < 5; i++) {
            antsImages[i] = new ImageView(antImageLeft);
            antsImages[i].setFitWidth(35);
            antsImages[i].setPreserveRatio(true);
            antsImages[i].setSmooth(true);

            AnchorPane.setLeftAnchor(antsImages[i], creepingGameApp.getAnts().get(i).getPosition() + 100.0);
            AnchorPane.setBottomAnchor(antsImages[i], 0.0);
            antsImageLayout.getChildren().add(antsImages[i]);
        }
    }

    @FXML
    private AnchorPane antsImageLayout;

    private ImageView[] antsImages = new ImageView[5];

    private void putAnts() {

    }

}
