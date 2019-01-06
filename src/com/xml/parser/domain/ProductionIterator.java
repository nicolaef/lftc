package com.xml.grammar.domain;

import java.util.List;

public class ProductionIterator {
    private Production production;
    private Integer location = 0;

    private Element getNext(){
        return production.getElements().get(location);
    }
    //return null or raise exception????
    private Element getPrev(){
        if (location-1 > 0)
            return production.getElements().get(location-1);
        return null;
    }
    //return null or raise exception????
    private Element goNext(){
        if (++location < production.getElements().size() - 1)
            return production.getElements().get(location);
        return null;
    }

    private List<Element> getPrevs(){
        return production.getElements().subList(0,location);
    }

    private List<Element> getNexts(){
        return production.getElements().subList(location,production.getElements().size());
    }

}
