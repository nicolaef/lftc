package com.xml.parser.domain;

import com.xml.grammar.domain.Element;

import java.util.ArrayList;
import java.util.List;

public class AnalysisTable {
    private List<AnalysisTableRow> table = new ArrayList<>();

    public AnalysisTable() {
    }

    public void addTableRow(AnalysisTableRow tableRow) {
        table.add(tableRow);
    }

    public State getValue(State s, Element e) {
        for (AnalysisTableRow tableRow : table) {
            if (tableRow.getState().equals(s)) {
                if (tableRow.getGotoList().get(e) != null) {
                    return tableRow.getGotoList().get(e);
                }
                return null;
            }
        }
        return null;
    }

    public Action getAction(State s) {
        for (AnalysisTableRow tableRow : table) {
            if (tableRow.getState().equals(s)){
                return tableRow.getAction();
            }
        }
        return null;
    }
}
