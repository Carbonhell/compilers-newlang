package esercitazione5.Utils;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.SyntaxTreeNodes.Expressions.UnaryExpr;
import esercitazione5.SyntaxTreeNodes.Visitable;
import esercitazione5.Visitor.Visitor;

/**
 * Special node added during semantic analysis to allow for type widening of expressions.
 * Represents an unary operator.
 */
public class Cast extends UnaryExpr {

    public Cast(Expr expr) {
        super(expr);
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitCast(this);
    }
}
