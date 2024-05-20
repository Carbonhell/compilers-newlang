package esercitazione4.SyntaxTreeNodes;

import java.util.ArrayList;

public class StatList{
    public ArrayList<Stat> statList;

    public StatList(Stat stat) {
        this.statList = new ArrayList<>();
        this.statList.add(stat);
    }
    public StatList(Stat stat, StatList statList) {
        this.statList = statList.statList;
        this.statList.add(stat);
    }
}
