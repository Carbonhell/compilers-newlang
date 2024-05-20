package esercitazione4.SyntaxTreeNodes;

import java.util.ArrayList;

public class DeclList {
    public ArrayList<VarDecl> varDecls;
    public ArrayList<FunDecl> funDecls;

    public DeclList() {
        this.varDecls = new ArrayList<>();
        this.funDecls = new ArrayList<>();
    }

    public DeclList(VarDecl decl, DeclList declList) {
        this.varDecls = declList.varDecls;
        this.funDecls = declList.funDecls;

        this.varDecls.add(decl);
    }

    public DeclList(FunDecl decl, DeclList declList) {
        this.varDecls = declList.varDecls;
        this.funDecls = declList.funDecls;

        this.funDecls.add(decl);
    }
}
