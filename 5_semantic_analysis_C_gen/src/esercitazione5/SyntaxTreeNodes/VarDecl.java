package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.LinkedHashMap;

public class VarDecl implements Visitable{
    // Nullable
    public Type type;
    public LinkedHashMap<Identifier, Expr> idsInit; // LinkedHashMap used to respect insertion order

    public VarDecl(Type type, IdInitList idInitList) {
        this.type = type;
        this.idsInit = idInitList.idsInit;
    }

    public VarDecl(IdInitObblList<?> idInitObblList) {
        this.idsInit = idInitObblList.idsInit;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitVarDecl(this);
    }
}
