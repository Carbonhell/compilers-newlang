package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class CharConstExpr implements Expr, Visitable {
    public char attribute;

    public CharConstExpr(String attribute) {
        this.attribute = attribute.charAt(0);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitCharConstExpr(this);
    }
}
