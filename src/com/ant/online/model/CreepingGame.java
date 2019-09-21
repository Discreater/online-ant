package com.ant.online.model;

import java.util.ArrayList;
import java.util.List;

public class CreepingGame {
    private int tick = 0;
    private int velocity = 1;
    private List<Integer> initPositions;
    private List<Boolean> initFacings;
    private List<Ant> ants;

    public CreepingGame(PlayRoom playRoom, List<Boolean> facings) {
        assert playRoom.positions.size() == facings.size();
        this.initPositions = playRoom.positions;
        this.initFacings = facings;
    }

    public void init() {
        this.ants = new ArrayList<>();
        int velocity = this.velocity;
        for (int i = 0; i < initPositions.size(); i++) {
            Integer position = this.initPositions.get(i);
            Boolean facing = this.initFacings.get(i);
            Ant ant = new Ant(position, velocity, facing);
            ants.add(ant);
        }
    }

    public void destroy() {

    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void nextTick() throws Exception{
        this.tick++;
        this.moveAll();
        this.bounceAll();
        this.turnBackAll();
    }

    private void moveAll() {
        for (int i = 0; i < this.ants.size(); i++) {
            Ant ant = this.ants.get(i);
            int position = ant.getPosition();
            if (ant.isFaceLeft()) {
                ant.setPosition(position - 1);
            } else {
                ant.setPosition(position + 1);
            }
        }
    }

    private void bounceAll() throws Exception {
        for (int i = 0; i < this.ants.size(); i++) {
            if (i < this.ants.size() - 1) {  // Not the last ant
                Ant ant = ants.get(i);
                Ant nextAnt = ants.get(i + 1);
                if (!ant.isFaceLeft()) {          // Facing right
                    int cmp = ant.compareTo(nextAnt);
                    if (cmp > 0) {
                        ant.setPosition(ant.getPosition() - 1);
                        ant.turnBack();
                        nextAnt.setPosition(nextAnt.getPosition() + 1);
                        nextAnt.turnBack();
                    } else if (cmp == 0) {
                        throw new Exception("Error detected in bounce all");
                    }
                }
            }
        }
    }

    private void turnBackAll() throws Exception {
        for (int i = 0; i < this.ants.size(); i++) {
            if (i < this.ants.size() - 1) {
                Ant ant = ants.get(i);
                Ant nextAnt = ants.get(i + 1);
                int cmp=ant.compareTo(nextAnt);
                if (cmp > 0){
                    ant.turnBack();
                    nextAnt.turnBack();
                }
                else if (cmp==0){
                    throw new Exception(("Error detected in turnBackAll"));
                }
                assert ant.compareTo(nextAnt) < 0;
            }
        }
    }
}
