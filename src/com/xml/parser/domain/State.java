package com.xml.parser.domain;

import com.xml.grammar.domain.NonTerminal;
import com.xml.parser.domain.actions.Shift;

import java.util.List;
import java.util.Objects;

public class State {
    private List<ProductionIterator> productionIterators;
    private NonTerminal nonTerminal;
    private Integer id;

    public State() {
        this.id = index++;
    }

    private static int index;

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public State(List<ProductionIterator> productionIterators, NonTerminal nonTerminal) {
        this.productionIterators = productionIterators;
        this.nonTerminal = nonTerminal;
        this.id = index++;
    }

    public List<ProductionIterator> getProductionIterators() {
        return productionIterators;
    }

    public void setProductionIterators(List<ProductionIterator> productionIterators) {
        this.productionIterators = productionIterators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(productionIterators, state.productionIterators);
    }

    @Override
    public int hashCode() {

        return Objects.hash(productionIterators);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("->").append("{");
        for (ProductionIterator pi: productionIterators) {
            sb.append("[").append(pi).append("], ");
        }
        sb.append("}");
        return sb.toString();
    }
}
