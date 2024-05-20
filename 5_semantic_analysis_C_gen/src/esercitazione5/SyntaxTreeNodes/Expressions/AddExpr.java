package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class AddExpr extends BinaryExpr {

    public AddExpr(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitAddExpr(this);
    }
}
