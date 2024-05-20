package esercitazione4.SyntaxTreeNodes;

import java.util.ArrayList;

public class VarDeclList {
    public ArrayList<VarDecl> varDecls;

    public VarDeclList() {
        this.varDecls = new ArrayList<>();
    }
    public VarDeclList(VarDecl varDecl, VarDeclList varDecls) {
        this.varDecls = varDecls.varDecls;
        this.varDecls.add(varDecl);
    }
}
