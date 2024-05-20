package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Scoped;
import esercitazione5.SyntaxTreeNodes.Visitable;
import esercitazione5.Utils.Scope;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class Identifier implements Expr, Visitable, Scoped {
    public String attribute;

    private String nodeType;

    private Scope scope;

    public Identifier(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitIdentifier(this);
    }

    @Override
    public String getType() {
        return this.nodeType;
    }

    @Override
    public void setType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public Scope getScope() {
        return this.scope;
    }

    @Override
    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
