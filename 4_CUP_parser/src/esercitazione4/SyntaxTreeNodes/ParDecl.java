package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione4.Visitor.Visitor;

import java.util.ArrayList;

public class ParDecl implements Visitable{
    public Type type;
    public ArrayList<Identifier> idList;
    public boolean isOut;

    public ParDecl(Type type, IdList idList, boolean isOut) {
        this.type = type;
        this.idList = idList.idList;
        this.isOut = isOut;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitParDecl(this);
    }
}
