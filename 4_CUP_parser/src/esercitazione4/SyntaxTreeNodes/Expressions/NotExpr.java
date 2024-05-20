package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class NotExpr implements Expr, Visitable {
    public Expr expr;

    public NotExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitNotExpr(this);
    }
}
