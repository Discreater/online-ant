package com.ant.online.model;

import java.util.ArrayList;
import java.util.List;

public class CreepingGame {
    protected List<Ant> ants;
    protected Stick stick;
    private int tick = 0;
    private int beginIndex;
    private int endIndex;

    public CreepingGame(List<Ant> ants, Stick stick) {
        this.ants=new ArrayList<>();
        this.ants.addAll(ants);
        this.stick=stick;
        beginIndex=0;
        endIndex=this.ants.size()-1;
    }

    public void init() {

    }

    public void start() {
        init();
        while (!isGameOver()) {
            try {
                System.out.println("In tick "+getTick());
                this.nextTick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        destroy();
    }

    public void destroy() {

    }

    public boolean isGameOver() {
        return beginIndex>endIndex;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public List<Ant> nextTick() throws Exception{
        this.setTick(this.tick+1);
        this.moveAll();
        this.bounceAll();
        this.turnBackAll();
        this.dropAll();
        return this.ants;
    }

    private void moveAll() {
        for (int i = beginIndex; i <= endIndex; i++) {
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
        for (int i = beginIndex; i <= endIndex; i++) {
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
        for (int i = beginIndex; i <= endIndex; i++) {
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

    private void dropAll() {
        Ant firstAnt=ants.get(beginIndex);
        Ant lastAnt=ants.get(endIndex);
        if (stick.onStick(firstAnt.getPosition())){
            beginIndex++;
            firstAnt.setOnline(false);
        }
        if(stick.onStick(lastAnt.getPosition())){
            endIndex--;
            lastAnt.setOnline(false);
        }
    }
}
