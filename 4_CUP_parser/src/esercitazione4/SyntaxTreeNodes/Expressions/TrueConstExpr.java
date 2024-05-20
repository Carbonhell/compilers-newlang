package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class TrueConstExpr implements Expr, Visitable {

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitTrueConstExpr(this);
    }
}
