package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione5.SyntaxTreeNodes.Expressions.StringConstExpr;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.ArrayList;

public class ReadStat implements Stat, Visitable{
    public ArrayList<Identifier> idList;
    // Nullable
    public StringConstExpr message;

    public ReadStat(IdList idList, StringConstExpr message) {
        this.idList = idList.idList;
        this.message = message;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitReadStat(this);
    }
}
