package com.xml.parser;

import com.xml.grammar.Grammar;
import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.NonTerminal;
import com.xml.grammar.domain.Production;
import com.xml.parser.domain.ProductionIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {


    private List<ProductionIterator> closure(ProductionIterator elem, Grammar grammar) {
        List<ProductionIterator> result = new ArrayList<>();
        result.add(elem);
        boolean updated = true;
        do {
            for (ProductionIterator prod : result) {
                Element e = prod.getNext();
                if (e instanceof NonTerminal) {
                    List<Production> productions = grammar.getProductions((NonTerminal) e);

                    List<ProductionIterator> iterators = productions
                            .stream()
                            .map(ProductionIterator::new)
                            .collect(Collectors.toList());
                    iterators.removeAll(result);
                    if (iterators.size() != 0) {
                        result.addAll(iterators);
                    } else {
                        updated = false;
                    }
                }
            }
        } while (updated);
        return result;
    }

}
