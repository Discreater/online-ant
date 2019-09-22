package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import com.ant.online.model.Ant;
import com.ant.online.model.CreepingGame;
import com.ant.online.model.PlayRoom;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RootLayoutController {
    public static final double stickTrueLength = 600.0;
    public static final double antImageWidth = 20.0;
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

    @FXML
    private Button commitButton;
    @FXML
    private Button setDefaultButton;

    // Reference to the main application
    private CreepingGameApp creepingGameApp;

    private boolean running = false;

    private Image antImageLeft;
    private Image antImageRight;

    @FXML
    private void initialize() {
        antImageLeft = new Image("file:resources/images/ant.png");
        antImageRight = new Image("file:resources/images/ant-reverse.png");
    }

    /**
     * When click the "恢复默认" button.
     */
    @FXML
    private void handleSetDefault() {
        this.creepingGameApp.setDefaultInput();
    }

    /**
     * When click the "确认" button
     */
    @FXML
    private void handleCommit(){
        try {
            int velocity = Integer.parseInt(velocityField.getText());
            int stickLength = Integer.parseInt(stickLengthField.getText());
            if (velocity <= 0 || stickLength <= 0) {
                throw new NumberFormatException();
            }
            String antPosition = antsPositionTA.getText();
            String[] antsPositions = antPosition.split("[ ,，:\n]");
            List<Integer> aPI = new ArrayList<>(antPosition.length());
            for (String aPS : antsPositions) {
                System.out.println(aPS);
                int ap = Integer.parseInt(aPS);
                if(ap < 0){
                    throw new NumberFormatException();
                }
                aPI.add(ap);
            }
            Collections.sort(aPI);

            // Format the input text field.
            setInputForm(velocity, stickLength, aPI);
            // Clear ant list
            creepingGameApp.getAnts().clear();

            creepingGameApp.setVelocity(velocity);
            creepingGameApp.getStick().setLength(stickLength);
            for (Integer ap : aPI) {
                Ant ant = new Ant(ap, velocity);
                creepingGameApp.getAnts().add(ant);
            }
        } catch (NumberFormatException e){
            System.out.println("Input invalid!");
        }
    }

    public void setInputForm(int velocity, int stickLength, @NotNull List<Integer> aPI) {
        velocityField.setText(Integer.toString(velocity));
        stickLengthField.setText(Integer.toString(stickLength));
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> iterator = aPI.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }
        antsPositionTA.setText(stringBuilder.toString());
    }

    /**
     * When click the "开始" button
     */
    @FXML
    private void handleStart() {
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
            antSerial.setText(Integer.toString(i));

            ImageView tmpImage = new ImageView(creepingGameApp.getAnts().get(i).isFaceLeft() ? antImageLeft : antImageRight);
            antImages.add(tmpImage);
            tmpImage.setFitWidth(antImageWidth);
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
        return ant.getPosition() * stickTrueLength / creepingGameApp.getStick().getLength() - antVBoxWidth / 2.0 + offset;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setCreepingGameApp(CreepingGameApp creepingGameApp) {
        this.creepingGameApp = creepingGameApp;
    }

    public void playAndPauseButtonSetState(CreepingGameApp.State state) {

    }
}
