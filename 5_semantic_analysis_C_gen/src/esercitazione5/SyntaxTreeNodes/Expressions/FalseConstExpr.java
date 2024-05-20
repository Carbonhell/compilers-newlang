package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Type;
import esercitazione5.SyntaxTreeNodes.Typed;
import esercitazione5.SyntaxTreeNodes.Visitable;
import esercitazione5.Visitor.Visitor;

public class FalseConstExpr implements Expr, Visitable, Typed {

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitFalseConstExpr(this);
    }

    @Override
    public String getType() {
        return Type.BOOL;
    }

    @Override
    public void setType(String nodeType) {
        // noop
    }
}
