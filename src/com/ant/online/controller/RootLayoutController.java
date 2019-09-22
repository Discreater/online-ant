package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import com.ant.online.model.Ant;
import com.ant.online.model.CreepingGame;
import com.ant.online.model.PlayRoom;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RootLayoutController {
    public static final double stickLength = 600.0;
    public static final double antWidth = 20.0;
    public static final double antVBoxWidth = 30.0;
    public static final double antVBoxHeight = 45;
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
    private void handleCommmit() {
        // TODO: initial whole game by the input data,
    }

    /**
     * When click the "开始" button
     */
    @FXML
    private void handleStart() {
        if (running) return;
        running = true;
        creepingGameApp.startPlay();
    }

    /**
     * When click the "下一帧" button or invoked by
     *
     * @return only for the {@link RootLayoutController#handleStart()} to invoke
     */
    @FXML
    private void handleNextTick() {
       creepingGameApp.nextTick();
    }


    private List<VBox> antVBoxes;
    private List<ImageView> antImages;

    public void putAnts() {
        int antAmount = creepingGameApp.getAnts().size();
        antVBoxes = new ArrayList<>(antAmount);
        antImages = new ArrayList<>(antAmount);
        for (int i = 0; i < antAmount; i++) {
            VBox tmpVBox = new VBox((i % 2) * 10 + 10.0);
            antVBoxes.add(tmpVBox);
            tmpVBox.setPrefHeight(antVBoxHeight);
            tmpVBox.setPrefWidth(antVBoxWidth);
            tmpVBox.setAlignment(Pos.BOTTOM_CENTER);
            AnchorPane.setBottomAnchor(tmpVBox, 0.0);
            AnchorPane.setLeftAnchor(tmpVBox, getTruePosition(creepingGameApp.getAnts().get(i)));

            Label antSerial = new Label();
            antSerial.setText(new Integer(i).toString());

            ImageView tmpImage = new ImageView(creepingGameApp.getAnts().get(i).isFaceLeft() ? antImageLeft : antImageRight);
            antImages.add(tmpImage);
            tmpImage.setFitWidth(antWidth);
            tmpImage.setPreserveRatio(true);
            tmpImage.setSmooth(true);
            tmpImage.setPickOnBounds(true);

            tmpVBox.getChildren().addAll(antSerial, tmpImage);
            antsImageLayout.getChildren().add(tmpVBox);
        }
    }

    public void changeAnts() {
        for (int i = 0; i < creepingGameApp.getAnts().size(); i++) {
            if (!creepingGameApp.getAnts().get(i).isOnline()) {
                antsImageLayout.getChildren().remove(antVBoxes.get(i));
            } else {
                AnchorPane.setLeftAnchor(antVBoxes.get(i), getTruePosition(creepingGameApp.getAnts().get(i)));
                antImages.get(i).setImage(creepingGameApp.getAnts().get(i).isFaceLeft() ? antImageLeft : antImageRight);
            }
        }
    }

    private double getTruePosition(@NotNull Ant ant) {
        return ant.getPosition() * stickLength / creepingGameApp.getStick().getLength() - antVBoxWidth / 2.0 + offset;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setCreepingGameApp(CreepingGameApp creepingGameApp) {
        this.creepingGameApp = creepingGameApp;
    }
}
