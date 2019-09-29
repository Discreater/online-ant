package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import com.ant.online.model.Ant;
import javafx.beans.value.ObservableValue;
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
    private static final double stickTrueLength = 600.0;
    private static final double antImageWidth = 20.0;
    private static final double antVBoxWidth = 30.0;
    private static final double antVBoxHeight = 45;
    private static final double offset = 100.0;

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
    private TableColumn<Ant, String> currentAntSerialColumn;
    @FXML
    private TableColumn<Ant, Integer> currentAntPositionColumn;
    @FXML
    private TableColumn<Ant, Boolean> currentAntFaceColumn;

    @FXML
    private Label currentGameNumberLabel;
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

    @FXML
    private Button startAndSkipButton;
    @FXML
    private Button playAndPauseButton;
    @FXML
    private Button nextConditionButton;

    @FXML
    private Slider speedSlider;

    // Reference to the main application
    private CreepingGameApp creepingGameApp;

    private Image antImageLeft;
    private Image antImageRight;

    @FXML
    private void initialize() {
        antImageLeft = new Image("file:resources/images/ant.png");
        antImageRight = new Image("file:resources/images/ant-reverse.png");
        currentAntSerialColumn.setCellFactory((col) ->
                new TableCell<Ant, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            this.setText(String.valueOf(this.getIndex()));
                        }
                    }
                }
        );
        currentAntPositionColumn.setCellValueFactory(cellDate -> cellDate.getValue().positionProperty().asObject());

        currentAntFaceColumn.setCellValueFactory(cellDate -> cellDate.getValue().faceLeftProperty());
        currentAntFaceColumn.setCellFactory((col) ->
                new TableCell<Ant, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item ? "左" : "右");
                        }
                    }
                }
        );
        speedSlider.setValue(55.0);
        speedSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) ->
            creepingGameApp.setAnimationSpeed(new_val.intValue())
        );
    }

    /**
     * When click the "恢复默认" button.
     */
    @FXML
    private void handleSetDefault() {
        if (creepingGameApp.getAppState() != CreepingGameApp.State.IDLE) return;
        this.creepingGameApp.setDefaultInput();
    }

    /**
     * When click the "确认" button
     */
    @FXML
    private void handleCommit() {
        if (creepingGameApp.getAppState() != CreepingGameApp.State.IDLE) return;
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
                int ap = Integer.parseInt(aPS);
                if (ap < 0) {
                    throw new NumberFormatException();
                }
                aPI.add(ap);
            }
            Collections.sort(aPI);

            // Format the input text field.
            setInputForm(velocity, stickLength, aPI);
            // Clear ant list
            creepingGameApp.getInitAnts().clear();

            creepingGameApp.setVelocity(velocity);
            creepingGameApp.getStick().setLength(stickLength);
            for (Integer ap : aPI) {
                Ant ant = new Ant(ap, velocity);
                creepingGameApp.getInitAnts().add(ant);
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("格式错误");
            alert.setHeaderText("请检查输入格式");
            alert.setContentText("输入必须为整数，各蚂蚁位置之间以逗号、空格、冒号或回车分隔.");

            alert.showAndWait();
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
     * When click the "开始 | 跳过动画" button.
     */
    @FXML
    private void handleStartOrSkip() {
        if (creepingGameApp.getAppState() == CreepingGameApp.State.IDLE) {
            creepingGameApp.setAppState(CreepingGameApp.State.PAUSED);
            creepingGameApp.startPlay();
        } else if (creepingGameApp.getAppState() == CreepingGameApp.State.PAUSED || creepingGameApp.getAppState() == CreepingGameApp.State.PLAYING) {
            creepingGameApp.setAppState(CreepingGameApp.State.STOPPING);
        }
    }

    /**
     * When click the "播放 | 暂停" button.
     */
    @FXML
    private void handlePlayOrPause() {
        CreepingGameApp.State state = creepingGameApp.getAppState();
        if (state == CreepingGameApp.State.PAUSED) {
            creepingGameApp.setAppState(CreepingGameApp.State.PLAYING);
        } else if (state == CreepingGameApp.State.PLAYING) {
            creepingGameApp.setAppState(CreepingGameApp.State.PAUSED);
        }
    }

    /**
     * When click the "下一情况" button.
     */
    @FXML
    private void handleNextCondition() {
        CreepingGameApp.State state = creepingGameApp.getAppState();
        if (state == CreepingGameApp.State.PAUSED || state == CreepingGameApp.State.PLAYING) {
            creepingGameApp.setAppState(CreepingGameApp.State.CHANGING);
        }
    }

    private List<VBox> antVBoxes;
    private List<ImageView> antImages;

    public void putAnts() {
        antsImageLayout.getChildren().clear();
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
        minTimeField.setText(Double.toString(creepingGameApp.getCurrentPlayingRoom().getCurrentMinAccurateTick()));
        maxTimeField.setText(Double.toString(creepingGameApp.getCurrentPlayingRoom().getCurrentMaxAccurateTick()));
        currentTimeLabel.setText(Integer.toString(creepingGameApp.getCurrentCreepingGame().getTick()));
    }

    public void gameInit(){
        currentGameNumberLabel.setText(Integer.toString(creepingGameApp.getCurrentCreepingGame().getGameNumber()));
    }

    public void roomFinish() {
        currentGameNumberLabel.setText("0");
        currentTimeLabel.setText("0");
    }

    private double getTruePosition(@NotNull Ant ant) {
        return ant.getPosition() * stickTrueLength / creepingGameApp.getStick().getLength() - antVBoxWidth / 2.0 + offset;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setCreepingGameApp(CreepingGameApp creepingGameApp) {
        this.creepingGameApp = creepingGameApp;
        this.currentAntTable.setItems(creepingGameApp.getAnts());
        this.creepingGameApp.setAnimationSpeed((int)speedSlider.getValue());
    }

    public void appStateChange() {
        CreepingGameApp.State state = creepingGameApp.getAppState();
        switch (state) {
            case PLAYING:
            case PAUSED:
                startAndSkipButton.setDisable(false);
                startAndSkipButton.setText("跳过动画");
                break;
            case IDLE:
                startAndSkipButton.setDisable(false);
                startAndSkipButton.setText("开始");
                break;
            default:
                startAndSkipButton.setDisable(true);
                break;
        }

        switch (state) {
            case PLAYING:
                playAndPauseButton.setDisable(false);
                playAndPauseButton.setText("暂停");
                break;
            case PAUSED:
                playAndPauseButton.setDisable(false);
                playAndPauseButton.setText("播放");
                break;
            default:
                playAndPauseButton.setDisable(true);
                break;
        }

        switch (state) {
            case PAUSED:
            case PLAYING:
                nextConditionButton.setDisable(false);
                break;
            default:
                nextConditionButton.setDisable(true);
                break;
        }

        switch (state) {
            case IDLE:
                setDefaultButton.setDisable(false);
                commitButton.setDisable(false);
                break;
            default:
                setDefaultButton.setDisable(true);
                commitButton.setDisable(true);
                break;
        }
    }
}
