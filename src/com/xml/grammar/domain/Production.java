package com.xml.grammar.domain;

import java.util.List;

public class Production {
    private List<Element> elements;

    public Production() {
    }

    public Production(List<Element> elements) {
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
