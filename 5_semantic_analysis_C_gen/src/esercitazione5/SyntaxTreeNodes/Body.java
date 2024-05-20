package esercitazione5.SyntaxTreeNodes;

import esercitazione5.Utils.Scope;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

import java.util.ArrayList;

public class Body implements Visitable, Scoped {
    public ArrayList<VarDecl> varDecls;
    public ArrayList<Stat> stats;

    /**
     * Used during semantic analysis to decide whether to enter a new scope or not when visiting this node.
     */
    public boolean definesScope = true;
    private Scope scope;

    public Body(VarDeclList varDeclList, StatList statList) {
        this.varDecls = varDeclList.varDecls;
        this.stats = statList.statList;
    }

    @Override
    public <T> T accept(Visitor<T> v) throws SemanticException {
        return v.visitBody(this);
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
