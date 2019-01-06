package com.xml.grammar.domain;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Element e:elements){
            if (e instanceof NonTerminal){
                sb.append(((NonTerminal) e).getName());
            } else {
                sb.append(((Terminal) e).getId());
            }
            sb.append(" ");
        }
        return  sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(elements, that.elements);
    }

}
