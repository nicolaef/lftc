package com.xml.parser.domain;

import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.Production;

import java.util.List;
import java.util.Objects;

public class ProductionIterator {
    private Production production;
    private Integer location;

    public ProductionIterator(Production production) {
        this.production = production;
        location = 0;
    }

    public ProductionIterator(Production production, Integer location) {
        this.production = production;
        this.location = location;
    }

    public Element getNext(){
        return production.getElements().get(location);
    }
    //return null or raise exception????
    public Element getPrev(){
        if (location-1 > 0)
            return production.getElements().get(location-1);
        return null;
    }
    //return null or raise exception????
    public Element goNext(){
        if (++location < production.getElements().size() - 1)
            return production.getElements().get(location);
        return null;
    }

    public List<Element> getPrevs(){
        return production.getElements().subList(0,location);
    }

    public List<Element> getNexts(){
        return production.getElements().subList(location,production.getElements().size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionIterator that = (ProductionIterator) o;
        return Objects.equals(production, that.production) &&
                Objects.equals(location, that.location);
    }
}