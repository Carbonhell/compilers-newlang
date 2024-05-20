package esercitazione4.SyntaxTreeNodes;

import esercitazione4.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione4.Visitor.Visitor;

import java.util.ArrayList;

public class FunDecl implements Visitable{
    public Identifier id;
    // Can be empty
    public ArrayList<ParDecl> parDeclList;
    public Type returnType;
    public Body body;

    public FunDecl(Identifier id, ParamDeclList paramDeclList, Type returnType, Body body) {
        this.id = id;
        this.parDeclList = paramDeclList.parDecls;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitFunDecl(this);
    }
}
