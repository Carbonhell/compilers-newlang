package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Expr;
import esercitazione4.Visitor.Visitor;

import java.util.ArrayList;

public class WriteStat implements Stat, Visitable{
    public ArrayList<Expr> exprList;
    public boolean isWriteln;

    public WriteStat(ExprList exprList, boolean isWriteln) {
        this.exprList = exprList.exprList;
        this.isWriteln = isWriteln;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitWriteStat(this);
    }
}
