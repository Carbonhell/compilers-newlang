package esercitazione5.SyntaxTreeNodes;

import java.util.ArrayList;

/**
 * Represents ParamDeclList and NonEmptyParamDeclList non-terminals.
 */
public class ParamDeclList {
    // Can be empty
    public ArrayList<ParDecl> parDecls;

    public ParamDeclList() {
        this.parDecls = new ArrayList<>();
    }
    public ParamDeclList(ParamDeclList parDecls) {
        this.parDecls = parDecls.parDecls;
    }
    public ParamDeclList(ParDecl parDecl) {
        this.parDecls = new ArrayList<>();
        this.parDecls.add(parDecl);
    }
    public ParamDeclList(ParDecl parDecl, ParamDeclList parDecls) {
        this.parDecls = parDecls.parDecls;
        this.parDecls.add(parDecl);
    }
}
