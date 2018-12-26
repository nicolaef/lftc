package com.xml;

import java.io.IOException;

import static com.xml.FileUtils.PrintTo2Files;

public class Main {

    public static final String PIF_FILE = "pif.out";
    public static final String ST_FILE = "st.out";
    public static final String COMBINED_FILE = "result.out";


    public static void main(String[] args) throws IOException {
        LexicalAnalyzer la = new LexicalAnalyzer();
        la.analize("code.txt");
        PrintTo2Files(la);
        la.getPif().forEach(i -> System.out.println(i + " "));
        la.getStList().forEach(System.out::println);

    }


}
