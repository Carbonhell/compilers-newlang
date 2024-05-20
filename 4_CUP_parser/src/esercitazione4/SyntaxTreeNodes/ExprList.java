package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Expr;

import java.util.ArrayList;

public class ExprList {
    public ArrayList<Expr> exprList;

    public ExprList() {
        this.exprList = new ArrayList<>();
    }

    public ExprList(Expr expr) {
        this.exprList = new ArrayList<>();
        this.exprList.add(expr);
    }
    public ExprList(ExprList exprList) {
        this.exprList = exprList.exprList;
    }
    public ExprList(Expr expr, ExprList exprList) {
        this.exprList = exprList.exprList;
        this.exprList.add(expr);
    }
}
