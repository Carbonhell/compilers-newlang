package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class NotExpr extends UnaryExpr {
    public NotExpr(Expr expr) {
        super(expr);
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitNotExpr(this);
    }
}
