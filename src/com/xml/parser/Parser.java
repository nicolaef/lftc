package com.xml.parser;

import com.xml.grammar.Grammar;
import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.NonTerminal;
import com.xml.grammar.domain.Production;
import com.xml.grammar.domain.Terminal;
import com.xml.parser.domain.ProductionIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private Grammar grammar;

    public Parser(Grammar g) {
        grammar = g;
        ProductionIterator prd = new ProductionIterator(g.getNonTerminals().get(28).getProductions().get(0));
        List<ProductionIterator> res = closure(prd);

        List<ProductionIterator> res2 = goTo(res,new Terminal(11));
        List<ProductionIterator> res3 = goTo(res2,res.get(12).getProduction().getElements().get(1));

    }

    private List<ProductionIterator> closure(ProductionIterator elem) {
        List<ProductionIterator> result = new ArrayList<>();
        result.add(elem);

        for (int i = 0; i < result.size(); i++) {
            ProductionIterator prod = result.get(i);
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
                }
            }
        }
        return result;
    }

    private List<ProductionIterator> goTo(List<ProductionIterator> s, Element element) {
        List<ProductionIterator> iterators = s
                .stream()
                .filter(i -> i.getNext().equals(element))
                .map(ProductionIterator::new)
                .collect(Collectors.toList());
        if (iterators.size() != 1){
            throw new RuntimeException("INCOMPATIBLE GRAMMAR");
        }
        return closure(iterators.get(0).goNext());
    }

}
