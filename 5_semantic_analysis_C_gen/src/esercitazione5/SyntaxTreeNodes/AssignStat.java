package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.ArrayList;

public class AssignStat implements Stat, Visitable{
    public ArrayList<Identifier> idList;
    public ArrayList<Expr> exprList;

    public AssignStat(IdList idList, ExprList exprList) throws Exception {
        this.idList = idList.idList;
        this.exprList = exprList.exprList;
        if(this.idList.size() != this.exprList.size()) {
            throw new Exception("The amount of identifiers should be equal to the amount of values supplied.");
        }
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitAssignStat(this);
    }
}
