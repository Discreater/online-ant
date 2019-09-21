package com.ant.online.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Model class for a Person.
 */
public class Ant {

    private final IntegerProperty position;
    private final IntegerProperty velocity;
    private final BooleanProperty faceLeft;
    private final IntegerProperty dropTime;
    private final BooleanProperty online;

    public Ant(Integer position, Integer velocity, Boolean faceLeft) {
        this.position = new SimpleIntegerProperty(position);
        this.velocity = new SimpleIntegerProperty(velocity);
        this.faceLeft = new SimpleBooleanProperty(faceLeft);

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
}
