package com.ant.online.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Model class for a Person.
 */
public class Ant implements Comparable<Ant>{

    private final IntegerProperty position;
    private final IntegerProperty velocity;
    private final BooleanProperty faceLeft;
    private final IntegerProperty dropTime;
    private final BooleanProperty online;

    public Ant(Integer position, Integer velocity) {
        this.position = new SimpleIntegerProperty(position);
        this.velocity = new SimpleIntegerProperty(velocity);
        this.faceLeft = new SimpleBooleanProperty(true);

        this.dropTime = new SimpleIntegerProperty(0);
        this.online = new SimpleBooleanProperty(true);
    }

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public int getVelocity() {
        return velocity.get();
    }

    public IntegerProperty velocityProperty() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity.set(velocity);
    }

    public boolean isFaceLeft() {
        return faceLeft.get();
    }

    public BooleanProperty faceLeftProperty() {
        return faceLeft;
    }

    public void setFaceLeft(boolean faceLeft) {
        this.faceLeft.set(faceLeft);
    }

    public int getDropTime() {
        return dropTime.get();
    }

    public IntegerProperty dropTimeProperty() {
        return dropTime;
    }

    public void setDropTime(int dropTime) {
        this.dropTime.set(dropTime);
    }

    public boolean isOnline() {
        return online.get();
    }

    public BooleanProperty onlineProperty() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online.set(online);
    }

    @Override
    public int compareTo(@NotNull Ant o) {
        int pos1=this.getPosition();
        int pos2=o.getPosition();
        if (pos1!=pos2){
            return pos1 - pos2;
        }else {
            boolean facing1=this.isFaceLeft();
            boolean facing2=o.isFaceLeft();
            if(facing1 && !facing2) {
                return -1;
            }else if (facing1 == facing2){
                return 0;
            }else {
                return 1;
            }
        }
    }

    public void turnBack() {
        this.setFaceLeft(!this.isFaceLeft());
    }
}
