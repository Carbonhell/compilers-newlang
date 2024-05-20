package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class ReturnStat implements Stat, Visitable{
    // Nullable
    public Expr returnVal;

    public ReturnStat(Expr returnVal) {
        this.returnVal = returnVal;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitReturnStat(this);
    }
}
