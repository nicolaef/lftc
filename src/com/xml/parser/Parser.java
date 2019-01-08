package com.xml.parser;

import com.xml.grammar.Grammar;
import com.xml.grammar.domain.*;
import com.xml.grammar.domain.Number;
import com.xml.parser.domain.*;
import com.xml.parser.domain.actions.Acceptance;
import com.xml.parser.domain.actions.Error;
import com.xml.parser.domain.actions.Reduction;
import com.xml.parser.domain.actions.Shift;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    private Grammar extendedGrammar;

    private Grammar grammar;

    private AnalysisTable analysisTable;

    private Map<Pair<Element, State>, State> gotoMap = new HashMap<>();

    private void extendGrammar() {
        extendedGrammar = new Grammar(grammar);

        NonTerminal nt = extendedGrammar.getStartingSymbol();
        List<Production> productions = Collections.singletonList(new Production(Collections.singletonList(nt)));
        NonTerminal newStart = new NonTerminal("S'", productions);

        extendedGrammar.addNonTerminal(newStart);
        extendedGrammar.setStartingSymbol(newStart.getName());
    }

    private Action getAction(State state) {
        for (ProductionIterator iterator : state.getProductionIterators()) {
            if (iterator.getNext() != null) {
                return new Shift();
            }
            Production p = iterator.getProduction();
            for (NonTerminal nonTerminal : grammar.getNonTerminals()) {
                List<Production> productions = nonTerminal
                        .getProductions()
                        .stream()
                        .filter(i -> i.equals(p))
                        .collect(Collectors.toList());
                if (productions.size() == 1)
                    return new Reduction(productions.get(0).getId());
            }
            if (state.getNonTerminal().getName().equals("S'") &&
                    state.getProductionIterators().size() == 1 &&
                    state.getProductionIterators().get(0).getProduction().getElements().get(0).equals(grammar.getStartingSymbol())) {
                return new Acceptance();
            }
            return new Error();
        }
        return new Error();
    }

    private void populateAnalysisTable() {
        analysisTable = new AnalysisTable();

        List<State> states = colCan();
        int i;
        for (i = 0; i < states.size(); i++) {
            State state = states.get(i);
            AnalysisTableRow tableRow = new AnalysisTableRow();
            tableRow.setState(state);
            tableRow.setAction(getAction(state));
            System.out.println(state.toString());
            for (Element elem : grammar.getAllElements()) {
                State goTo = goTo(state, elem);
                if (goTo != null) {
                    List<State> states2 = states.stream().filter(p->p.equals(goTo)).collect(Collectors.toList());
                    if (states2.size() == 0)
                        tableRow.getGotoList().put(elem, goTo);
                    else
                        tableRow.getGotoList().put(elem,states2.get(0));
                }
            }
            analysisTable.addTableRow(tableRow);
        }
    }

    public List<Integer> syntacticAnalysis(List<Element> input) {
        Map<Integer, Production> productionMap = grammar.getProductionsMap();
        Integer state = 0;
        Stack<Element> alpha = new Stack<>();
        alpha.push(new NonTerminal("END"));
        alpha.push(new Number(0));
        Stack<Element> beta = new Stack<>();
        for (int i=input.size()-1; i >= 0; i--) {
            beta.push(input.get(i));
        }

        Stack<Integer> phi = new Stack<>();
        boolean end = false;

        do {
            final int finalState = state;
            AnalysisTableRow row = analysisTable
                    .getTable()
                    .stream()
                    .filter(i-> i.getState().getId() == finalState)
                    .collect(Collectors.toList())
                    .get(0);

            Action action = row.getAction();
            if (action instanceof Shift) {
                Element temp = beta.pop();
                state = row.getGotoList().get(temp).getId();
                alpha.push(temp);
                alpha.push(new Number(state));
            } else if (action instanceof Reduction) {
                Reduction reduction = (Reduction) action;
                Production prod = productionMap.get(reduction.getProduction());
                for (Element e : prod.getElements()) {
                    alpha.pop();
                    alpha.pop();
                }
                int num = ((Number) alpha.peek()).getNumber();
                state = analysisTable
                        .getTable()
                        .stream()
                        .filter(i-> i.getState().getId() == num)
                        .collect(Collectors.toList())
                        .get(0)
                        .getGotoList()
                        .get(prod.getLhs())
                        .getId();
                alpha.push(prod.getLhs());
                alpha.push(new Number(state));
                phi.push(reduction.getProduction());
            } else if (action instanceof Acceptance) {
                System.out.println("SUCCESS");
                end = true;
            } else {
                System.out.println("ERROR");
                end = true;
            }
        } while (!end);
        return phi;
    }

    public Parser(Grammar g) {
        grammar = g;
        extendGrammar();
        populateAnalysisTable();


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
                        .map(pi -> new ProductionIterator(pi, nonTerminal))
                        .collect(Collectors.toList());
                iterators.removeAll(result);
                if (iterators.size() != 0) {
                    result.addAll(iterators);
                }
            }
        }
        return new State(result, elem.getNonTerminal());
    }

    private State goTo(State s, Element element) {
        if (gotoMap.get(new Pair<>(element, s)) != null) {
            return gotoMap.get(new Pair<>(element, s));
        }
        List<ProductionIterator> iterators = s
                .getProductionIterators()
                .stream()
                .filter(i -> i.getNext() != null && i.getNext().equals(element))
                .map(ProductionIterator::new)
                .collect(Collectors.toList());
        if (iterators.size() > 1) {
            iterators.forEach(System.out::println);
            throw new RuntimeException("INCOMPATIBLE GRAMMAR");
        } else if (iterators.size() == 0) {
            return null;
        }
        State result = closure(iterators.get(0).goNext());
        gotoMap.put(new Pair<>(element, s), result);
        return result;
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
