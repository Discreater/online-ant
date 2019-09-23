package com.ant.online.controller;

import com.ant.online.CreepingGameApp;
import com.ant.online.model.PlayRoom;
import javafx.application.Platform;

public class AnimationThread extends Thread {

    private CreepingGameApp app = null;
    private RootLayoutController controller = null;

    public AnimationThread(CreepingGameApp app) {
        super("Animation Thread");
        this.app = app;
        this.controller = app.getRootLayoutController();
    }

    @Override
    public void run() {
        boolean toBreak = false;
        while (!toBreak) {
            CreepingGameApp.State currentState = app.getAppState();
            switch (currentState) {
                case IDLE:
                    toBreak = true;
                    break;
                case PAUSED:
                    break;
                case PLAYING:
                    try {
                        if (!this.app.getCurrentCreepingGame().isGameOver()) {
                            Thread.sleep(40);
                            this.app.getCurrentCreepingGame().nextTick();
                            this.changeAnts();
                        } else {
                            if (!this.app.nextGame()) {
                                this.app.setAppState(CreepingGameApp.State.IDLE);
                                toBreak = true;
                            } else {
                                this.putAnts();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CHANGING:
                    this.skipCurrentGame();
                    if (this.app.nextGame()) {
                        this.putAnts();
                        this.app.setAppState(CreepingGameApp.State.PAUSED);
                    } else {
                        this.app.setAppState(CreepingGameApp.State.IDLE);
                        toBreak = true;
                    }
                    break;
                case STOPPING:
                    this.skipAllRest();
                    this.changeAnts();
                    this.app.setAppState(CreepingGameApp.State.IDLE);
                    toBreak = true;
                    break;
                default:
                    System.err.println("Error state detected!");
                    break;
            }
            if (toBreak) {
                break;
            }
        }
        this.app.setAppState(CreepingGameApp.State.IDLE);
        PlayRoom playRoom=this.app.getCurrentPlayingRoom();
        playRoom.finish();
        System.out.println("Thread terminated.");
    }

    private void putAnts() {
        Platform.runLater(() -> {
            this.controller.putAnts();
        });
    }

    private void changeAnts() {
        Platform.runLater(() -> {
            this.controller.changeAnts();
        });
    }

    private void skipCurrentGame() {
        this.app.getCurrentCreepingGame().moveOn();
    }

    private void skipAllRest() {
        while (this.app.nextGame()) {
            this.skipCurrentGame();
        }
    }
}
