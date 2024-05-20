package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class RealConstExpr implements Expr, Visitable {
    public double attribute;

    public RealConstExpr(String attribute) {
        this.attribute = Double.parseDouble(attribute);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitRealConstExpr(this);
    }
}
