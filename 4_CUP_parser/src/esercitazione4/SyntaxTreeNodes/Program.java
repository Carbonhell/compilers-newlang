package esercitazione4.SyntaxTreeNodes;

import esercitazione4.Visitor.Visitor;

import java.util.ArrayList;

public class Program implements Visitable{
    public ArrayList<VarDecl> varDeclList;
    public ArrayList<FunDecl> funDeclList;
    public FunDecl mainFunDecl; // Specific field for the main function instead of keeping it in funDeclList

    public Program(DeclList preMainDeclList, MainFunDecl mainFunDecl, DeclList postMainDeclList) {
        this.varDeclList = preMainDeclList.varDecls;
        this.funDeclList = preMainDeclList.funDecls;

        this.varDeclList.addAll(postMainDeclList.varDecls);
        this.funDeclList.addAll(postMainDeclList.funDecls);
        this.mainFunDecl = mainFunDecl.funDecl;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitProgram(this);
    }
}
