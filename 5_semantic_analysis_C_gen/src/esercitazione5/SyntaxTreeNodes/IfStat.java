package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Expr;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

public class IfStat implements Stat, Visitable{
    public Expr condition;
    public Body body;
    // Nullable
    public Body elseNode;

    public IfStat(Expr condition, Body body, Else elseNode) {
        this.condition = condition;
        this.body = body;
        if(elseNode != null) {
            this.elseNode = elseNode.body;
        }
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitIfStat(this);
    }
}
