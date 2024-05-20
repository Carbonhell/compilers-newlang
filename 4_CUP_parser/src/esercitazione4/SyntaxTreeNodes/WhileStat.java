package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Expr;
import esercitazione4.Visitor.Visitor;

public class WhileStat implements Stat, Visitable{
    public Expr condition;
    public Body body;

    public WhileStat(Expr condition, Body body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitWhileStat(this);
    }
}
