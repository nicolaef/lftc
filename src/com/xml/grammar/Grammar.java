package com.xml.grammar;

import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.NonTerminal;
import com.xml.grammar.domain.Production;
import com.xml.grammar.domain.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {

    private Map<String, NonTerminal> nonTerminals = new HashMap<>();
    private Map<Integer, Terminal> terminals = new HashMap<>();
    private String startingSymbol;

    public Grammar(String filename) throws FileNotFoundException {
        readGrammarFromFile(filename);
    }

    public Grammar(Grammar g) {
        nonTerminals = new HashMap<>(g.nonTerminals);
        terminals = new HashMap<>(g.terminals);
        startingSymbol = g.startingSymbol;
    }

    public NonTerminal getStartingSymbol() {
        return nonTerminals.get(startingSymbol);
    }

    public void addNonTerminal(NonTerminal nt) {
        nonTerminals.put(nt.getName(), nt);
    }

    public void setStartingSymbol(String startingSymbol) {
        this.startingSymbol = startingSymbol;
    }

    public NonTerminal getNonTerminalByName(String name) {
        return nonTerminals.get(name);
    }

    public List<Production> getProductions(NonTerminal nonTerminal) {
        return nonTerminals.get(nonTerminal.getName()).getProductions();
    }

    public Map<Integer, Production> getProductionsMap() {
        List<Production> productions = new ArrayList<>();
        nonTerminals.values().forEach(
                i -> {
                    i.getProductions().forEach(k->k.setLhs(i));
                    productions.addAll(i.getProductions());
                });
        Map<Integer, Production> result = new HashMap<>();
        productions.forEach(i -> result.put(i.getId(), i));
        return result;
    }

    public List<Element> getAllElements() {
        List<Element> result = new ArrayList<>(nonTerminals.values());
        result.addAll(terminals.values());
        return result;
    }

    public List<NonTerminal> getNonTerminals() {
        return new ArrayList<>(nonTerminals.values());
    }

    public List<Terminal> getTerminals() {
        return new ArrayList<>(terminals.values());
    }

    private void readGrammarFromFile(String filename) throws FileNotFoundException {
        //FIRST SET EPSILON
        nonTerminals.put("Epsilon", new NonTerminal("Epsilon", new ArrayList<>()));


        Scanner scanner = new Scanner(new File(filename));
        String nonTerminalsString = scanner.nextLine();
        Arrays.stream(nonTerminalsString.split(" ")).forEach(i -> nonTerminals.put(i, new NonTerminal(i)));

        String terminalsString = scanner.nextLine();
        Arrays.stream(terminalsString.split(" ")).forEach(i -> terminals.put(Integer.parseInt(i), new Terminal(Integer.parseInt(i))));

        startingSymbol = scanner.nextLine();
        while (scanner.hasNextLine()) {
            String nonTerminalDefinition = scanner.nextLine();
            List<String> nameAndProductions = Arrays.asList(nonTerminalDefinition.split("->"));
            NonTerminal nonTerminal = nonTerminals.get(nameAndProductions.get(0));
            nonTerminal.setProductions(productionsFromString(nameAndProductions.get(1)));
        }
    }

    private List<Production> productionsFromString(String production) {
        List<String> productions = Arrays.asList(production.split("\\|"));
        List<Production> result = new ArrayList<>();
        for (String prod : productions) {
            List<String> elems = Arrays.asList(prod.split(" "));
            List<Element> resultPart = new ArrayList<>();
            for (String elem : elems) {
                if (elem.matches("[0-9]*")) {
                    resultPart.add(new Terminal(Integer.parseInt(elem)));
                } else {
                    resultPart.add(nonTerminals.get(elem));
                }
            }
            result.add(new Production(resultPart));
        }
        return result;
    }
}
