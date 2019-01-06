package com.xml.parser;

import com.xml.grammar.Grammar;
import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.NonTerminal;
import com.xml.grammar.domain.Production;
import com.xml.parser.domain.ProductionIterator;
import com.xml.parser.domain.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private Grammar grammar;

    private void extendGrammar() {
        // NODE: this is currently destructive (changes the current grammar instead of creating a new one) - might be cause of bugs
        NonTerminal nt = grammar.getStartingSymbol();
        List<Production> productions = Collections.singletonList(new Production(Collections.singletonList(nt)));
        NonTerminal newStart = new NonTerminal("extendedStart", productions);
        grammar.addNonTerminal(newStart);
        grammar.setStartingSymbol(newStart.getName());
    }

    public Parser(Grammar g) {
        grammar = g;
        extendGrammar();

        List<State> states = colCan();
    }

    private State closure(ProductionIterator elem) {
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
        return new State(result);
    }

    private State goTo(State s, Element element) {
        List<ProductionIterator> iterators = s
                .getProductionIterators()
                .stream()
                .filter(i -> i.getNext() != null && i.getNext().equals(element))
                .map(ProductionIterator::new)
                .collect(Collectors.toList());
        if (iterators.size() > 1) {
            throw new RuntimeException("INCOMPATIBLE GRAMMAR");
        } else if (iterators.size() == 0) {
            return null;
        }
        return closure(iterators.get(0).goNext());
    }

    private List<State> colCan() {
        List<State> result = new ArrayList<>();
        State s0 = closure(
                new ProductionIterator(
                        grammar.getStartingSymbol().getProductions().get(0)
                )
        );
        boolean modified = false;
        result.add(s0);
        do {
            for (int i = 0; i < result.size(); i++) {
                State state = result.get(i);
                for (Element element : grammar.getAllElements()) {
                    State gotoResult = goTo(state, element);
                    if (gotoResult != null && !result.contains(gotoResult)) {
                        result.add(gotoResult);
                        modified = true;
                    } else {
                        modified = false;
                    }
                }
            }
        } while (modified);
        return result;
    }
}
