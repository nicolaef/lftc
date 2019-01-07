package com.xml.parser.domain.actions;

import com.xml.parser.domain.Action;

public class Reduction implements Action {
    private int production;

    public Reduction(int production) {
        this.production = production;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }
}
