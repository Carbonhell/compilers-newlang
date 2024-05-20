package esercitazione5.SyntaxTreeNodes;

import java.util.ArrayList;

public class StatList{
    public ArrayList<Stat> statList;

    public StatList(Stat stat) {
        this.statList = new ArrayList<>();
        // the StatList production has at least 1 stat, but the stat itself can be null
        if(stat != null) {
            this.statList.add(stat);
        }
    }
    public StatList(Stat stat, StatList statList) {
        this.statList = statList.statList;
        if(stat != null) {
            this.statList.add(stat);
        }
    }
}
