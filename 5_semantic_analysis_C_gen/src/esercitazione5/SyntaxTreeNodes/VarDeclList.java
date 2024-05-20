package esercitazione5.SyntaxTreeNodes;

import java.util.ArrayList;

public class VarDeclList {
    public ArrayList<VarDecl> varDecls;

    public VarDeclList() {
        this.varDecls = new ArrayList<>();
    }
    public VarDeclList(VarDecl varDecl, VarDeclList varDecls) {
        this.varDecls = varDecls.varDecls;
        // Order of insertion is reversed due to the syntax rule (VarDecl, VarDeclList) so we fix it here
        this.varDecls.add(0, varDecl);
    }
}
