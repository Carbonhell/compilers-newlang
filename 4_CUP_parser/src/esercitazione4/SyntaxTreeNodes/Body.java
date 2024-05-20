package esercitazione4.SyntaxTreeNodes;

import esercitazione4.Visitor.Visitor;

import java.util.ArrayList;

public class Body implements Visitable {
    public ArrayList<VarDecl> varDecls;
    public ArrayList<Stat> stats;

    public Body(VarDeclList varDeclList, StatList statList) {
        this.varDecls = varDeclList.varDecls;
        this.stats = statList.statList;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitBody(this);
    }
}
