package com.ant.online.model;

import java.util.*;

public class PlayRoom implements Iterator<CreepingGame> {
    private List<Ant> ants;
    private List<Ant> antsCopy;
    private Stick stick;
    private boolean hasNext;

    public PlayRoom(List<Ant> ants, Stick stick) {
        this.ants = ants;
        this.stick = stick;
        Collections.sort(ants);
        this.antsCopy = new ArrayList<>();
        for (int i = 0; i < ants.size(); i++) {
            Ant ant = ants.get(i);
            this.antsCopy.add(new Ant(ant));
        }
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
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        for (int i = 0; i < ants.size(); i++) {
            Ant ant = ants.get(i);
            Ant antCopy = antsCopy.get(i);
            Ant.copy(antCopy, ant);
        }
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
        } else {
            Ant ant = ants.get(index);
            ant.turnBack();
            if (ant.isFaceLeft()) {
                recursiveNextAnts(ants, index + 1);
            }
        }
    }
}
