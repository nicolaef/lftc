package com.xml.grammar.domain;

import java.util.List;
import java.util.Objects;

public class NonTerminal implements Element {
    private String name;
    private List<Production> productions;

    public NonTerminal() {
    }

    public NonTerminal(String name) {
        this.name = name;
    }

    public NonTerminal(String name, List<Production> productions) {
        this.name = name;
        this.productions = productions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    @Override
    public String toString() {
        return "NonTerminal{" +
                "name='" + name + '\'' +
                ", productions=" + productions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonTerminal that = (NonTerminal) o;
        return Objects.equals(name, that.name);
    }

}
