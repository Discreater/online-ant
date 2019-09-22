package com.ant.online.model;

import java.util.ArrayList;
import java.util.List;

public class CreepingGame {
    private List<Ant> ants;
    private Stick stick;
    private int tick = 0;
    private int moveCount = 0;
    private int tickLoopTimes = 1;
    private int gameNumber = 0;
    private int beginIndex;
    private int endIndex;
    private int endTick;

    public CreepingGame(List<Ant> ants, Stick stick) {
        this.ants = new ArrayList<>();
        this.ants.addAll(ants);
        this.stick = stick;
        init();
    }

    public void init() {
        beginIndex = 0;
        endIndex = this.ants.size() - 1;
        moveCount = 0;
        int velocity = this.ants.get(0).getVelocity();
        tickLoopTimes = velocity;
        endTick = 0;
        gameNumber=0;
        for (int i=ants.size()-1;i>=0;i--) {
            gameNumber<<=1;
            if (!ants.get(i).isFaceLeft()) {
                gameNumber++;
            }
        }
    }


    public void destroy() {

    }

    public boolean isGameOver() {
        if (beginIndex > endIndex) {
            System.out.println("Game "+gameNumber+" terminated at tick " + tick);
            setEndTick(tick);
        }
        return beginIndex > endIndex;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void nextTick() throws Exception {
        this.setTick(this.getTick()+1);
        System.out.println("Tick " + tick + " has just passed.");
        for (int i = 0; i < tickLoopTimes; i++) {
            if (isGameOver()) {
                break;
            }
            move();
        }

    }

    public int getEndTick() {
        return endTick;
    }

    public void setEndTick(int endTick) {
        this.endTick = endTick;
    }

    public void move() throws Exception {
        this.setMoveCount(this.getMoveCount()+1);

        this.moveAll();
        this.bounceAll();
        this.turnBackAll();
        this.dropAll();
        System.out.println("Movement " + moveCount + " has just happened.");
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

    private void bounceAll() {
        for (int i = beginIndex; i <= endIndex; i++) {
            if (i < endIndex) {  // Not the last ant
                Ant ant = ants.get(i);
                Ant nextAnt = ants.get(i + 1);
                if (!ant.isFaceLeft()) {          // Facing right
                    if (ant.getPosition() > nextAnt.getPosition()) {
                        ant.setPosition(ant.getPosition() - 1);
                        ant.turnBack();
                        nextAnt.setPosition(nextAnt.getPosition() + 1);
                        nextAnt.turnBack();
                        System.out.println("Bounced ant " + i + " and " + i + 1);
                    }
                }
            }
        }
    }

    private void turnBackAll() throws Exception {
        for (int i = beginIndex; i <= endIndex; i++) {
            if (i < endIndex) {
                Ant ant = ants.get(i);
                Ant nextAnt = ants.get(i + 1);
                int cmp = ant.compareTo(nextAnt);
                if (cmp > 0) {
                    ant.turnBack();
                    nextAnt.turnBack();
                    System.out.println("Turned back " + i + "and" + Integer.toString(i + 1));
                } else if (cmp == 0) {
                    throw new Exception(("Error detected in turnBackAll: " + Integer.toString(i)));
                }
                assert ant.compareTo(nextAnt) < 0;
            }
        }
    }

    private void dropAll() {
        Ant firstAnt = ants.get(beginIndex);
        Ant lastAnt = ants.get(endIndex);
        if (!stick.onStick(firstAnt.getPosition())) {
            beginIndex++;
            firstAnt.setOnline(false);
            firstAnt.setDropTime(this.tick);
            System.out.println("ant " + firstAnt + " is dropping");
        }
        if (!stick.onStick(lastAnt.getPosition())) {
            endIndex--;
            lastAnt.setOnline(false);
            lastAnt.setDropTime(this.tick);
            System.out.println("ant " + lastAnt + " is dropping");
        }
    }
}
