package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class GEExpr extends BinaryExpr {
    public GEExpr(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitGEExpr(this);
    }
}
