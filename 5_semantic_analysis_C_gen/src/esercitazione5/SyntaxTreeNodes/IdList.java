package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;

import java.util.ArrayList;

public class IdList{
    public ArrayList<Identifier> idList;

    public IdList(Identifier id) {
        this.idList = new ArrayList<>();
        this.idList.add(id);
    }
    public IdList(Identifier id, IdList idList) {
        this.idList = idList.idList;
        this.idList.add(id);
    }
}
