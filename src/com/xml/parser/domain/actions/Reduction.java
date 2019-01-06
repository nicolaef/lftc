package com.xml.parser.domain.actions;

import com.xml.parser.domain.Action;

public class Reduction implements Action {
    private int state;

    public Reduction(int state) {
        this.state = state;
    }
}
