package com.ant.online.controller;

import com.ant.online.CreepingGame;

public class RootLayoutController {
    // Reference to the main application
    private CreepingGame creepingGame;

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setCreepingGame(CreepingGame creepingGame) {
        this.creepingGame = creepingGame;
    }

}
