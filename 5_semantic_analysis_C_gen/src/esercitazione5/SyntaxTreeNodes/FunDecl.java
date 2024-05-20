package esercitazione5.SyntaxTreeNodes;

import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;
import esercitazione5.Utils.Scope;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.ArrayList;

public class FunDecl implements Visitable, Scoped{
    public Identifier id;
    // Can be empty
    public ArrayList<ParDecl> parDeclList;
    public Type returnType;
    public Body body;

    private Scope scope;

    public FunDecl(Identifier id, ParamDeclList paramDeclList, Type returnType, Body body) {
        this.id = id;
        this.parDeclList = paramDeclList.parDecls;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitFunDecl(this);
    }

    @Override
    public Scope getScope() {
        return this.scope;
    }

    @Override
    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
