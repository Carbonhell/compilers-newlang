package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione4.SyntaxTreeNodes.Expressions.StringConstExpr;
import esercitazione4.Visitor.Visitor;

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
    public <T> T accept(Visitor<T> v) {
        return v.visitReadStat(this);
    }
}
