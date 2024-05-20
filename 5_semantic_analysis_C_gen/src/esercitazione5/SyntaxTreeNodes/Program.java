package esercitazione5.SyntaxTreeNodes;

import esercitazione5.Utils.Scope;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.ArrayList;

public class Program implements Visitable, Scoped{
    public ArrayList<VarDecl> varDeclList;
    public ArrayList<FunDecl> funDeclList;
    public FunDecl mainFunDecl; // Specific field for the main function instead of keeping it in funDeclList

    private Scope scope;

    public Program(DeclList preMainDeclList, MainFunDecl mainFunDecl, DeclList postMainDeclList) {
        this.varDeclList = preMainDeclList.varDecls;
        this.funDeclList = preMainDeclList.funDecls;

        this.varDeclList.addAll(postMainDeclList.varDecls);
        this.funDeclList.addAll(postMainDeclList.funDecls);
        this.mainFunDecl = mainFunDecl.funDecl;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitProgram(this);
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
