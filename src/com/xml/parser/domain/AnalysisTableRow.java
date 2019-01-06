package com.xml.parser.domain;

import com.xml.grammar.domain.Element;

import java.util.HashMap;
import java.util.Map;

public class AnalysisTableRow {

    private Action action;
    private State state;
    private Map<Element, State> gotoMap = new HashMap<>();

    public AnalysisTableRow() {
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Map<Element, State> getGotoList() {
        return gotoMap;
    }

    public void setGotoList(Map<Element, State> gotoList) {
        this.gotoMap = gotoList;
    }

}
