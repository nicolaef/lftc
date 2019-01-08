package com.xml;

import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LexicalAnalyzer {
    //parsing outputs
    private List<Pair<Integer, Integer>> pif = new ArrayList<>(); //program internal form
    private BinaryTree st = new BinaryTree(); // symbol table
    private List<Pair<Integer, String>> stList = new ArrayList<>();

    //language specific
    private Map<String, Integer> codification = new HashMap<>();

    //parsing helpers
    private String buffer = "";

    //location in code (for errors)
    private int line = 1;
    private int col = 0;

    private void initializeExtra() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("codification.2txt"));
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (!s.equals("")) {
                List<String> tokens = Arrays.stream(s.split(" ")).collect(Collectors.toList());
                codification.put(tokens.get(1), Integer.parseInt(tokens.get(0)));
            }
        }
    }

    public LexicalAnalyzer() throws FileNotFoundException {
        initializeExtra();
    }

    private void addToPif(int key, int value) {
        buffer = "";
        pif.add(new Pair<>(key, value));
    }

    private void getCurrentToken() {
        if (!buffer.isEmpty()) {
            if (codification.containsKey(buffer))
                addToPif(codification.get(buffer), -1);
            else if (isConstant(buffer)) {
                addToPif(1, st.poz(buffer));
            } else if (isIdentifier(buffer)) {
                addToPif(0, st.poz(buffer));
            } else
                throw new RuntimeException("Illegal identifier \"" + buffer + "\" at line " + line + " column " + (col - buffer.length()) + ".");
            buffer = "";
        }
    }

    public void analize(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int ch;
        while ((ch = br.read()) != -1) {
            Character c = (char) ch;
            col += 1;
            if (isWhitespace(c)) {
                if (c.equals('\n')) {
                    line += 1;
                    col = 0;
                }
                getCurrentToken();
                continue;
            }
            if (inAlphabet(c)) {
                if (codification.containsKey(c.toString())) {
                    getCurrentToken();
                    buffer += c;
                    getCurrentToken();
                    continue;
                }
                buffer += c.toString();
                continue;
            }
            if (c == '\'') {
                ch = br.read();
                Character c1 = (char) ch;
                ch = br.read();
                Character c2 = (char) ch;
                if (c2 != '\'')
                    throw new RuntimeException("Illegal constant character definition at line " + line + " and column " + (col));
                if (inAlphabet(c1)) {
                    addToPif(1, st.poz(c1.toString()));
                    continue;
                }
            }
            throw new RuntimeException("Illegal character '" + c + "' at line " + line + " and column " + (col - 1) + ".");
        }
        if (!buffer.isEmpty())
            getCurrentToken();

        //map the previous values of the identifiers as the new values that are gonna be in the list symbol table
        List<Pair<Integer, String>> btp = st.ordered();
        List<Integer> btpInt = btp.stream().map(Pair::getKey).collect(Collectors.toList());
        for (int i = 0; i < pif.size(); i++) {
            if (pif.get(i).getKey().equals(0) || pif.get(i).getKey().equals(1))
                pif.set(i, new Pair<>(pif.get(i).getKey(), btpInt.indexOf(pif.get(i).getValue())));
        }
        //build the symbol table as a list
        for (int i = 0; i < btp.size(); i++) {
            stList.add(new Pair<>(i, btp.get(i).getValue()));
        }
    }

    private boolean isConstant(String s) {
        return s.matches("^[0-9]+$");

    }

    private boolean isIdentifier(String s) {
        return s.matches("^[a-zA-Z][a-zA-Z0-9]{0,7}$");
    }

    private boolean inAlphabet(Character c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (codification.containsKey(c.toString()) && codification.get(c.toString()) <= 25);
    }

    private boolean isWhitespace(Character c) {
        return c.equals('\n') || c.equals('\t') || c.equals(' ') || c.equals('\r');

    }

    public List<Pair<Integer, Integer>> getPif() {
        return pif;
    }

    public List<Pair<Integer, String>> getStList() {
        return stList;
    }

}
