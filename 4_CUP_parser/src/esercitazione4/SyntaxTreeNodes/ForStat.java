package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione4.Visitor.Visitor;

public class ForStat implements Stat, Visitable{
    public Identifier tempVar;
    public int from;
    public int to;
    public Body body;

    public ForStat(Identifier tempVar, String from, String to, Body body) {
        this.tempVar = tempVar;
        this.from = Integer.parseInt(from);
        this.to = Integer.parseInt(to);
        this.body = body;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitForStat(this);
    }
}
