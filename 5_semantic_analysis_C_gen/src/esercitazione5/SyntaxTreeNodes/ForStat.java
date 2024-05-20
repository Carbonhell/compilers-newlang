package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

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
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitForStat(this);
    }
}
