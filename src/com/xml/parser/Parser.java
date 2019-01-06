package com.xml.parser;

import com.xml.grammar.Grammar;
import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.NonTerminal;
import com.xml.grammar.domain.Production;
import com.xml.parser.domain.*;
import com.xml.parser.domain.actions.Acceptance;
import com.xml.parser.domain.actions.Error;
import com.xml.parser.domain.actions.Reduction;
import com.xml.parser.domain.actions.Shift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private Grammar extendedGrammar;

    private Grammar grammar;

    private void extendGrammar() {
        extendedGrammar = new Grammar(grammar);

        NonTerminal nt = extendedGrammar.getStartingSymbol();
        List<Production> productions = Collections.singletonList(new Production(Collections.singletonList(nt)));
        NonTerminal newStart = new NonTerminal("S'", productions);

        extendedGrammar.addNonTerminal(newStart);
        extendedGrammar.setStartingSymbol(newStart.getName());
    }

    private Action getAction(State state){
        for (ProductionIterator iterator: state.getProductionIterators()){
            if (iterator.getNext()!=null){
                return new Shift();
            }
            Production p = iterator.getProduction();
            for (NonTerminal nonTerminal : grammar.getNonTerminals()){
                if (nonTerminal.getProductions().contains(p))
                    //TODO: replace this with the id of the production, also add ids to productions
                    return new Reduction(state.getId());
            }
            if (state.getNonTerminal().getName().equals("S'") &&
                    state.getProductionIterators().size() == 1 &&
                    state.getProductionIterators().get(0).getNonTerminal().equals(grammar.getStartingSymbol())){
                return new Acceptance();
            }
            return new Error();
        }
        return new Error();
    }

    public Parser(Grammar g) {
        grammar = g;
        extendGrammar();

        AnalysisTable analysisTable = new AnalysisTable();

        List<State> states = colCan();
        for (int i = 0; i<states.size(); i++){
            State state = states.get(i);
            state.setId(i);

            AnalysisTableRow tableRow = new AnalysisTableRow();
            tableRow.setState(state);
            tableRow.setAction(getAction(state));
            System.out.println(state.toString());
            for (Element elem : grammar.getAllElements()){
                State goTo = goTo(state,elem);
                if (goTo != null){
                    tableRow.getGotoList().put(elem,goTo);
                }
            }
            analysisTable.addTableRow(tableRow);
        }
        System.out.println("WOOOOOOOOOOO");
    }

    private State closure(ProductionIterator elem) {
        List<ProductionIterator> result = new ArrayList<>();
        result.add(elem);

        for (int i = 0; i < result.size(); i++) {
            ProductionIterator prod = result.get(i);
            Element e = prod.getNext();
            if (e instanceof NonTerminal) {
                NonTerminal nonTerminal = (NonTerminal) e;
                List<Production> productions = extendedGrammar.getProductions(nonTerminal);

                List<ProductionIterator> iterators = productions
                        .stream()
                        .map(pi -> new ProductionIterator(pi, nonTerminal ))
                        .collect(Collectors.toList());
                iterators.removeAll(result);
                if (iterators.size() != 0) {
                    result.addAll(iterators);
                }
            }
        }
        return new State(result,elem.getNonTerminal());
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
                        extendedGrammar.getStartingSymbol().getProductions().get(0),
                        extendedGrammar.getStartingSymbol()
                )
        );
        boolean modified;
        result.add(s0);
        do {
            modified = false;
            for (int i = 0; i < result.size(); i++) {
                State state = result.get(i);
                for (Element element : extendedGrammar.getAllElements()) {
                    State gotoResult = goTo(state, element);
                    if (gotoResult != null && !result.contains(gotoResult)) {
                        result.add(gotoResult);
                        modified = true;
                    }
                }
            }
        } while (modified);
        return result;
    }
}
