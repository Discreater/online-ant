package com.ant.online.model;

import java.util.*;

public class PlayRoom implements Iterator<CreepingGame> {
    private List<Ant> ants;
    private List<Ant> antsCopy;
    private Stick stick;

    private boolean hasNext;
    private boolean allOver = false;

    private int currentMaxEndTick = 0;
    private int currentMinEndTick = 0x3f3f3f3f; // Infinity

    private double currentMaxAccurateTick = 0.0;
    private double currentMinAccurateTick = 1e9; // Infinity

    private CreepingGame lastGivenGame = null;

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

    public boolean isAllOver() {
        return allOver;
    }

    public void setAllOver(boolean allOver) {
        this.allOver = allOver;
    }

    public int getCurrentMaxEndTick() {
        return currentMaxEndTick;
    }

    public void setCurrentMaxEndTick(int currentMaxEndTick) {
        this.currentMaxEndTick = currentMaxEndTick;
    }

    public int getCurrentMinEndTick() {
        return currentMinEndTick;
    }

    public void setCurrentMinEndTick(int currentMinEndTick) {
        this.currentMinEndTick = currentMinEndTick;
    }

    public double getCurrentMaxAccurateTick() {
        return currentMaxAccurateTick;
    }

    public void setCurrentMaxAccurateTick(double currentMaxAccurateTick) {
        this.currentMaxAccurateTick = currentMaxAccurateTick;
    }

    public double getCurrentMinAccurateTick() {
        return currentMinAccurateTick;
    }

    public void setCurrentMinAccurateTick(double currentMinAccurateTick) {
        this.currentMinAccurateTick = currentMinAccurateTick;
    }

    public void init() {
        for (Ant ant : antsCopy) {
            ant.setOnline(true);
            ant.setFaceLeft(true);
        }
        hasNext = true;
    }

    public void finishOne(CreepingGame creepingGame) {
        assert creepingGame.isGameOver();
        int endTick = creepingGame.getEndTick();
        int moveCount = creepingGame.getMoveCount();
        double accurateEndTick = creepingGame.getAccurateEndTick();
        this.setCurrentMaxEndTick(Math.max(this.getCurrentMaxEndTick(),endTick));
        this.setCurrentMinEndTick(Math.min(this.getCurrentMinEndTick(),endTick));
        this.setCurrentMaxAccurateTick(Math.max(this.getCurrentMaxAccurateTick(),accurateEndTick));
        this.setCurrentMinAccurateTick(Math.min(this.getCurrentMinAccurateTick(),accurateEndTick));
        System.out.println("Finished game "+creepingGame.getGameNumber() +" in "+moveCount+" movement(s)");
        System.out.println("Current max and min ticks: "+this.getCurrentMaxEndTick()+","+this.getCurrentMinEndTick());
    }

    public void finish() {
        assert this.isAllOver();
        int max,min; double maxAccurate, minAccurate;
        max=this.getCurrentMaxEndTick();
        min=this.getCurrentMinEndTick();
        maxAccurate=this.getCurrentMaxAccurateTick();
        minAccurate=this.getCurrentMinAccurateTick();
        System.out.println("Max and min tick:" + max+", "+min+", or accurately "+maxAccurate+","+minAccurate);
    }


    @Override
    public boolean hasNext() {
        if (!hasNext) {
            this.setAllOver(true);
        }
        return hasNext;
    }

    @Override
    public CreepingGame next() {
        if (this.lastGivenGame != null) {
            assert this.lastGivenGame.isGameOver();
            this.finishOne(this.lastGivenGame);
        }
        if (!hasNext) {
            throw new NoSuchElementException("No more conditions!");
        }
        for (int i = 0; i < ants.size(); i++) {
            Ant ant = ants.get(i);
            Ant antCopy = antsCopy.get(i);
            Ant.copy(antCopy, ant);
        }
        this.lastGivenGame = new CreepingGame(this.ants, this.stick);
        nextAnts(this.antsCopy);
        return this.lastGivenGame;
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
