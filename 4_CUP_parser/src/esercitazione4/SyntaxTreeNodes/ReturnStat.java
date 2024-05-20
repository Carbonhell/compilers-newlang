package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Expr;
import esercitazione4.Visitor.Visitor;

public class ReturnStat implements Stat, Visitable{
    // Nullable
    public Expr returnVal;

    public ReturnStat(Expr returnVal) {
        this.returnVal = returnVal;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitReturnStat(this);
    }
}
