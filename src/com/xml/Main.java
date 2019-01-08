package com.xml;

import com.xml.grammar.Grammar;
import com.xml.grammar.domain.Element;
import com.xml.grammar.domain.Terminal;
import com.xml.parser.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.xml.FileUtils.PrintTo2Files;

public class Main {

    public static final String PIF_FILE = "pif.out";
    public static final String ST_FILE = "st.out";
    public static final String COMBINED_FILE = "result.out";


    public static void main(String[] args) throws IOException {
        LexicalAnalyzer la = new LexicalAnalyzer();
        la.analize("code.2txt");
        PrintTo2Files(la);
//        la.getStList().forEach(System.out::println);
        Grammar g = new Grammar("grammar.2txt");
        List<Element> elements = new ArrayList<>();

        la.getPif().forEach(i -> elements.add(new Terminal(i.getKey())));



        Parser p = new Parser(g);
        List<Integer> productions = p.syntacticAnalysis(elements);
        StringBuilder sb = new StringBuilder();
        productions.forEach(i->sb.append(i).append(", "));
        System.out.println("PRODUCTIONS:");
        System.out.println(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        la.getPif().forEach(i->sb2.append(i.getKey()).append(", "));
        System.out.println("PIF:");
        System.out.println(sb2.toString());
    }


}
