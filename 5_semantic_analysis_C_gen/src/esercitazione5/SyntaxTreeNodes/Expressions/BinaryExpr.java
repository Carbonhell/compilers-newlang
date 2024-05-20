package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Visitable;

public abstract class BinaryExpr implements Expr, Visitable {
    public Expr first;
    public Expr second;
    protected String nodeType;

    public BinaryExpr(Expr first, Expr second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String getType() {
        return this.nodeType;
    }

    @Override
    public void setType(String nodeType) {
        this.nodeType = nodeType;
    }
}
