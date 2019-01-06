package com.xml.parser.domain;

import java.util.List;
import java.util.Objects;

public class State {
    private List<ProductionIterator> productionIterators;

    public State(List<ProductionIterator> productionIterators) {
        this.productionIterators = productionIterators;
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
        sb.append("{");
        for (ProductionIterator pi: productionIterators) {
            sb.append("[").append(pi.toString()).append("], ");
        }
        sb.append("}");
        return sb.toString();
    }
}
