package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.ArrayList;

public class WriteStat implements Stat, Visitable{
    public ArrayList<Expr> exprList;
    public boolean isWriteln;

    public WriteStat(ExprList exprList, boolean isWriteln) {
        this.exprList = exprList.exprList;
        this.isWriteln = isWriteln;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitWriteStat(this);
    }
}
