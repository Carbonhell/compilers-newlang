package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class IntegerConstExpr implements Expr, Visitable {
    public int attribute;

    public IntegerConstExpr(String attribute) {
        this.attribute = Integer.parseInt(attribute);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitIntegerConstExpr(this);
    }
}
