package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Const;
import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;

import java.util.LinkedHashMap;

public class IdInitObblList <T extends Expr>{
    public LinkedHashMap<Identifier, Expr> idsInit;

    public IdInitObblList(Identifier identifier, Const<T> constExpr) {
        this.idsInit = new LinkedHashMap<>();
        this.idsInit.put(identifier, constExpr.attribute);
    }

    public IdInitObblList(Identifier identifier, Const<T> constExpr, IdInitObblList<T> idList) {
        this.idsInit = idList.idsInit;
        this.idsInit.put(identifier, constExpr.attribute);
    }
}
