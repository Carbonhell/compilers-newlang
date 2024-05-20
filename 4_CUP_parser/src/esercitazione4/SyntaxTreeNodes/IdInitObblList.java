package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Const;
import esercitazione4.SyntaxTreeNodes.Expressions.Expr;
import esercitazione4.SyntaxTreeNodes.Expressions.Identifier;

import java.util.LinkedHashMap;

public class IdInitObblList <T extends Expr>{
    public LinkedHashMap<Identifier, Expr> idsInit;
    //public Type inferredType; // TODO move to semantic analysis

    public IdInitObblList(Identifier identifier, Const<T> constExpr) {
        this.idsInit = new LinkedHashMap<>();
        this.idsInit.put(identifier, constExpr.attribute);
        //this.inferredType = constExpr.type;
    }

    public IdInitObblList(Identifier identifier, Const<T> constExpr, IdInitObblList<T> idList) throws Exception {
        /*if(!idList.inferredType.equals(constExpr.type)) {
            throw new Exception("Inferred id initializations must all have the same type.");
        }*/
        this.idsInit = idList.idsInit;
        this.idsInit.put(identifier, constExpr.attribute);
    }
}
