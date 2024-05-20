package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Expr;
import esercitazione4.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione4.Visitor.Visitor;

import java.util.ArrayList;

/**
 * The syntax tree specification implies an attribute on each Expr to specify whether it's an in or out expr.
 * The grammar does not have such info though, therefore it must be obtained from the semantic analysis by checking the
 * function definition.
 */
public class FunCall implements Stat, Expr, Visitable{
    public Identifier id;

    public ArrayList<Expr> exprList;

    public FunCall(Identifier id, ExprList exprList) {
        this.id = id;
        this.exprList = exprList.exprList;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitFunCall(this);
    }
}
