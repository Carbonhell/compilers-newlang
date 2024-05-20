package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

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
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitParDecl(this);
    }
}
