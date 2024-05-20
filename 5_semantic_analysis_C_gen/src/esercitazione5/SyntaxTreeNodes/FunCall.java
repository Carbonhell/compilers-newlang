package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione5.Utils.Scope;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.ArrayList;

/**
 * The syntax tree specification implies an attribute on each Expr to specify whether it's an in or out expr.
 * The grammar does not have such info though, therefore it must be obtained from the semantic analysis by checking the
 * function definition.
 */
public class FunCall implements Stat, Expr, Visitable, Typed, Scoped{
    public Identifier id;

    public ArrayList<Expr> exprList;

    private String nodeType;

    public boolean isStat = false;

    private Scope scope;

    public FunCall(Identifier id, ExprList exprList) {
        this.id = id;
        this.exprList = exprList.exprList;
    }

    public FunCall asStat() {
        this.isStat = true;
        return this;
    }
    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitFunCall(this);
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
        return scope;
    }

    @Override
    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
