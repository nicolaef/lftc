package com.xml.grammar.domain;

public class Terminal implements Element {
    private int id;

    public Terminal() {
    }

    public Terminal(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
