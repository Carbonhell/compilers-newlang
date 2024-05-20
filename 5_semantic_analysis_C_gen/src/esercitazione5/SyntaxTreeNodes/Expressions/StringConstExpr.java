package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Type;
import esercitazione5.SyntaxTreeNodes.Typed;
import esercitazione5.SyntaxTreeNodes.Visitable;
import esercitazione5.Visitor.Visitor;

public class StringConstExpr implements Expr, Visitable, Typed {
    public String attribute;

    public StringConstExpr(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitStringConstExpr(this);
    }

    @Override
    public String getType() {
        return Type.STRING;
    }

    @Override
    public void setType(String nodeType) {
        // noop
    }
}
