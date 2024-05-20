package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Type;
import esercitazione5.SyntaxTreeNodes.Typed;
import esercitazione5.SyntaxTreeNodes.Visitable;
import esercitazione5.Visitor.Visitor;

public class CharConstExpr implements Expr, Visitable, Typed {
    public char attribute;

    public CharConstExpr(String attribute) {
        this.attribute = attribute.charAt(0);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitCharConstExpr(this);
    }

    @Override
    public String getType() {
        return Type.CHAR;
    }

    @Override
    public void setType(String nodeType) {
        // noop
    }
}
