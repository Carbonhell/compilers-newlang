package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;

import java.util.LinkedHashMap;

public class IdInitList{
    public LinkedHashMap<Identifier, Expr> idsInit;
    public IdInitList(Identifier id) {
        this.idsInit = new LinkedHashMap<>();
        this.idsInit.put(id, null);
    }

    public IdInitList(Identifier id, Expr expr) {
        this.idsInit = new LinkedHashMap<>();
        this.idsInit.put(id, expr);
    }

    public IdInitList(Identifier id, IdInitList idInitList) {
        this.idsInit = idInitList.idsInit;
        this.idsInit.put(id, null);
    }

    public IdInitList(Identifier id, Expr expr, IdInitList idInitList) {
        this.idsInit = idInitList.idsInit;
        this.idsInit.put(id, expr);
    }
}
