package com.xml.grammar.domain;

import java.util.Objects;

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

    @Override
    public String toString() {
        return id + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terminal terminal = (Terminal) o;
        return id == terminal.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
