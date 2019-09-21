package com.ant.online.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PlayRoom implements Iterator<CreepingGame> {
    private List<Ant> ants;
    private List<Ant> antsCopy;
    private Stick stick;
    private boolean hasNext;

    public PlayRoom(List<Ant> ants, Stick stick) {
        this.ants = ants;
        this.stick = stick;
        Collections.sort(ants);
        this.antsCopy = new ArrayList<>(ants);
        //Collections.copy(antsCopy, ants);
        init();
    }

    public void init() {
        for (Ant ant : antsCopy) {
            ant.setOnline(true);
            ant.setFaceLeft(true);
        }
        hasNext = true;
    }

    public void destroy() {

    }


    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public CreepingGame next() {
        Collections.copy(this.ants, this.antsCopy);
        CreepingGame creepingGame = new CreepingGame(this.ants, this.stick);
        nextAnts(this.antsCopy);
        return creepingGame;
    }

    private void nextAnts(List<Ant> ants) {
        this.recursiveNextAnts(ants, 0);
    }

    private void recursiveNextAnts(List<Ant> ants, int index) {
        int maxIndex = ants.size() - 1;
        if (index > maxIndex) {
            hasNext = false;
            return;
        } else {
            Ant ant = ants.get(index);
            ant.turnBack();
            if (!ant.isFaceLeft()) {
                recursiveNextAnts(ants, index + 1);
            }
        }
    }
}
