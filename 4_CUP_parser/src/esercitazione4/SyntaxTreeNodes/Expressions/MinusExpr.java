package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class MinusExpr implements Expr, Visitable {
    public Expr expr;

    public MinusExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitMinusExpr(this);
    }
}
