package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Type;
import esercitazione5.SyntaxTreeNodes.Typed;
import esercitazione5.SyntaxTreeNodes.Visitable;
import esercitazione5.Visitor.Visitor;

public class RealConstExpr implements Expr, Visitable, Typed {
    public double attribute;

    public RealConstExpr(String attribute) {
        this.attribute = Double.parseDouble(attribute);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitRealConstExpr(this);
    }

    @Override
    public String getType() {
        return Type.FLOAT;
    }

    @Override
    public void setType(String nodeType) {
        // noop
    }
}
