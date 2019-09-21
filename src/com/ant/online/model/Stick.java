package com.ant.online.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Stick {
    private final IntegerProperty length;
    public Stick(int length) {
        this.length=new SimpleIntegerProperty(length);
    }

    public int getLength() {
        return length.get();
    }

    public IntegerProperty lengthProperty() {
        return length;
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    public boolean onStick(int position) {
        return position >=0 && position < this.getLength();
    }
}
