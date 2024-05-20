package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class WhileStat implements Stat, Visitable{
    public Expr condition;
    public Body body;

    public WhileStat(Expr condition, Body body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitWhileStat(this);
    }
}
