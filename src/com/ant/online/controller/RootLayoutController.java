package com.ant.online.controller;

import com.ant.online.CreepingGameApp;

public class RootLayoutController {
    // Reference to the main application
    private CreepingGameApp creepingGameApp;

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setCreepingGameApp(CreepingGameApp creepingGameApp) {
        this.creepingGameApp = creepingGameApp;
    }

}
