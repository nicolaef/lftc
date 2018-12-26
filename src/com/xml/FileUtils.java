package com.xml;

import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static com.xml.Main.*;

public class FileUtils {
    public static void PrintErrorToFile(Exception e) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pr = new PrintWriter(COMBINED_FILE, "UTF-8");
        pr.println(e.toString());
        pr.close();
    }

    public static void PrintPifToFile(LexicalAnalyzer la, PrintWriter pw) {
        StringBuilder s = new StringBuilder();
        s.append("PIF:\n");
        for (Pair<Integer, Integer> p : la.getPif()) {
            s.append(p.getKey())
                    .append(",")
                    .append(p.getValue())
                    .append("\n");

        }
        pw.print(s.toString());
    }

    public static void PrintSTToFile(LexicalAnalyzer la, PrintWriter pw) {
        StringBuilder s = new StringBuilder();
        s.append("ST:\n");
        for (Pair<Integer, String> p : la.getStList()) {
            s.append(p.getKey())
                    .append(",")
                    .append(p.getValue())
                    .append("\n");
        }
        pw.print(s.toString());
    }

    public static void PrintToFile(LexicalAnalyzer la) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pr = new PrintWriter(COMBINED_FILE, "UTF-8");
        PrintPifToFile(la, pr);
        PrintSTToFile(la, pr);
        pr.println();
        pr.close();
    }

    public static void PrintTo2Files(LexicalAnalyzer la) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pr = new PrintWriter(PIF_FILE, "UTF-8");
        PrintPifToFile(la, pr);
        pr.close();
        pr = new PrintWriter(ST_FILE, "UTF-8");
        PrintSTToFile(la, pr);
        pr.close();
    }
}
