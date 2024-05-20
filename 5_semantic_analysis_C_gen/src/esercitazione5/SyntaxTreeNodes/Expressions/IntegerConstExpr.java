package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Type;
import esercitazione5.SyntaxTreeNodes.Typed;
import esercitazione5.SyntaxTreeNodes.Visitable;
import esercitazione5.Visitor.Visitor;

public class IntegerConstExpr implements Expr, Visitable, Typed {
    public int attribute;

    public IntegerConstExpr(String attribute) {
        this.attribute = Integer.parseInt(attribute);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitIntegerConstExpr(this);
    }

    @Override
    public String getType() {
        return Type.INT;
    }

    @Override
    public void setType(String nodeType) {
        // noop
    }
}
