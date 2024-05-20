package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class StrConcatExpr extends BinaryExpr {
    public StrConcatExpr(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitStrConcatExpr(this);
    }
}
