package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class SubExpr implements Expr, Visitable {
    public Expr first;
    public Expr second;

    public SubExpr(Expr first, Expr second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitSubExpr(this);
    }
}
