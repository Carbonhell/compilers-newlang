package esercitazione4.SyntaxTreeNodes.Expressions;

import esercitazione4.SyntaxTreeNodes.Visitable;
import esercitazione4.Visitor.Visitor;

public class Identifier implements Expr, Visitable {
    public String attribute;

    public Identifier(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitIdentifier(this);
    }
}
