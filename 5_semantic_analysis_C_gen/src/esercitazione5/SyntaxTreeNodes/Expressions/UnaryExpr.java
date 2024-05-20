package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Visitable;

public abstract class UnaryExpr implements Expr, Visitable {
    public Expr expr;
    protected String nodeType;

    public UnaryExpr(Expr expr) {
        this.expr = expr;
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
