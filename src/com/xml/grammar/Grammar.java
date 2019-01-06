package com.xml.grammar;

import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.NonTerminal;
import com.xml.grammar.domain.Production;
import com.xml.grammar.domain.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {
    public Grammar() throws FileNotFoundException {
        readGrammarFromFile("grammar.txt");
    }

    Map<String, NonTerminal> nonTerminals = new HashMap<>();
    Map<Integer, Terminal> terminals = new HashMap<>();
    String startingSymbol;

    public List<Production> getProductions(NonTerminal nonTerminal){
        return nonTerminals.get(nonTerminal.getName()).getProductions();
    }

    private void readGrammarFromFile(String filename) throws FileNotFoundException {
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
            for (String elem: elems) {
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