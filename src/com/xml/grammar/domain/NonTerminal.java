package com.xml.grammar.domain;

import java.util.List;

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
}
