package com.ant.online.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayRoom {
    protected List<Integer> positions=new ArrayList<>();
    protected int verlocity;
    public PlayRoom(List<Integer> positions, int verlocity) {
        this.positions.addAll(positions);
        this.positions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        this.verlocity=verlocity;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public int getVerlocity() {
        return verlocity;
    }

    public void setVerlocity(int verlocity) {
        this.verlocity = verlocity;
    }
}
