package esercitazione5.Visitor;

import esercitazione5.SyntaxTreeNodes.*;
import esercitazione5.SyntaxTreeNodes.Expressions.*;
import esercitazione5.Utils.Cast;
import esercitazione5.Utils.ScopeEntry;
import esercitazione5.Utils.SemanticException;
import esercitazione5.Utils.SymbolTable;

import java.util.*;

public class SemanticVisitor implements Visitor<Void> {
    public SymbolTable typeEnv;

    public SemanticVisitor() {
        this.typeEnv = new SymbolTable();
    }

    private void addFunDeclEntry(FunDecl funDecl) throws SemanticException {
        Map<String, Object> extraData = new LinkedHashMap<>();
        this.typeEnv.addEntry(funDecl.id, ScopeEntry.SymbolType.FUNCTION, funDecl.returnType.type, extraData);
        this.typeEnv.enterScope(funDecl);
        for (ParDecl parDecl : funDecl.parDeclList) {
            parDecl.accept(this);

            // Add params to the function extra data to type check future fun calls
            for (Identifier id : parDecl.idList) {
                extraData.put(id.attribute, new ParDeclInfo(parDecl.type.type, parDecl.isOut));
            }
        }
        this.typeEnv.exitScope();
    }
    @Override
    public Void visitProgram(Program p) throws SemanticException {
        this.typeEnv.enterScope(p);

        for (VarDecl varDecl : p.varDeclList) {
            varDecl.accept(this);
        }
        // If a semantic exception is thrown again, we're probably dealing with circular definitions
        ArrayList<Expr> processableExprs = this.typeEnv.processFRefs();
        for(Expr processableExpr: processableExprs) {
            processableExpr.accept(this);
        }

        // Do a first pass to add all the declared functions to the global scope, so they can be referenced before definition.
        for (FunDecl funDecl : p.funDeclList) {
            this.addFunDeclEntry(funDecl);
        }
        this.addFunDeclEntry(p.mainFunDecl);

        for (FunDecl funDecl : p.funDeclList) {
            funDecl.accept(this);
        }
        p.mainFunDecl.accept(this);

        this.typeEnv.exitScope();

        return null;
    }

    // Handled directly on the node
    @Override
    public Void visitTrueConstExpr(TrueConstExpr trueConstExpr) {
        trueConstExpr.setType(Type.BOOL);
        return null;
    }

    // Handled directly on the node
    @Override
    public Void visitIntegerConstExpr(IntegerConstExpr integerConstExpr) {
        integerConstExpr.setType(Type.INT);
        return null;
    }

    // Handled directly on the node
    @Override
    public Void visitRealConstExpr(RealConstExpr realConstExpr) {
        realConstExpr.setType(Type.FLOAT);
        return null;
    }

    // Handled directly on the node
    @Override
    public Void visitStringConstExpr(StringConstExpr stringConstExpr) {
        stringConstExpr.setType(Type.STRING);
        return null;
    }

    // Handled directly on the node
    @Override
    public Void visitCharConstExpr(CharConstExpr charConstExpr) {
        charConstExpr.setType(Type.CHAR);
        return null;
    }

    @Override
    public Void visitType(Type type) {
        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl varDecl) throws SemanticException {
        for (Map.Entry<Identifier, Expr> entry : varDecl.idsInit.entrySet()) {
            Identifier id = entry.getKey();
            Expr expr = entry.getValue();

            // Type inference is handled during the syntax parsing stage.
            String type;
            if(varDecl.type != null) {
                type = varDecl.type.type;
            } else {
                type = expr.getType();
            }
            this.typeEnv.addEntry(id, ScopeEntry.SymbolType.VAR, type);

            id.accept(this);
            try {
                if (expr != null) {
                    expr.accept(this);
                }
            } catch (SemanticException ex) {
                if(ex.kind != SemanticException.Kind.Scope) {
                    throw ex;
                }
                // Used for ordered var declarations: the semantic exception is thrown globally only after
                // all varDecls have been analyzed and there's still a pending fRef
                this.typeEnv.addFRef(id, expr);
            }
        }

        return null;
    }

    // Data container for function type env extra data
    record ParDeclInfo(String type, boolean isOut) {
    }

    @Override
    public Void visitFunDecl(FunDecl funDecl) throws SemanticException {
        // Fun declarations are added to the global scope in the Program node so that order of definition doesn't matter
        // This includes parameter declarations in the funDecl scope
        this.typeEnv.enterScope(funDecl);

        // The return value is simulated with a variable
        this.typeEnv.addEntry(new Identifier("return"), ScopeEntry.SymbolType.RETURN, funDecl.returnType.type);

        funDecl.body.definesScope = false;
        funDecl.body.accept(this);
        this.typeEnv.exitScope();

        if(!funDecl.returnType.type.equals(Type.VOID)) {
            boolean returnStmtFound = false;
            for(Stat stat: funDecl.body.stats) {
                if(stat instanceof ReturnStat) {
                    returnStmtFound = true;
                }
            }
            if(!returnStmtFound) {
                throw new SemanticException("Missing return statement");
            }
        }

        return null;
    }

    @Override
    public Void visitBody(Body body) throws SemanticException {
        if (body.definesScope) {
            this.typeEnv.enterScope(body);
        }
        for (VarDecl varDecl :
                body.varDecls) {
            varDecl.accept(this);
        }
        // If a semantic exception is thrown again, we're probably dealing with circular definitions
        ArrayList<Expr> processableExprs = this.typeEnv.processFRefs();
        for(Expr processableExpr: processableExprs) {
            processableExpr.accept(this);
        }

        for (Stat stat :
                body.stats) {
            stat.accept(this);
        }

        if (body.definesScope) {
            this.typeEnv.exitScope();
        }

        return null;
    }

    @Override
    public Void visitIfStat(IfStat ifStat) throws SemanticException {
        ifStat.condition.accept(this);
        if(!ifStat.condition.getType().equals(Type.BOOL)) {
            throw new SemanticException("If condition must be of type boolean");
        }
        ifStat.body.accept(this);
        if(ifStat.elseNode != null) {
            ifStat.elseNode.accept(this);
        }

        return null;
    }

    @Override
    public Void visitWhileStat(WhileStat whileStat) throws SemanticException {
        whileStat.condition.accept(this);
        if(!whileStat.condition.getType().equals(Type.BOOL)) {
            throw new SemanticException("While condition must be of type boolean");
        }

        whileStat.body.accept(this);
        return null;
    }

    @Override
    public Void visitForStat(ForStat forStat) throws SemanticException {
        this.typeEnv.addEntry(forStat.tempVar, ScopeEntry.SymbolType.VAR, Type.INT);
        forStat.tempVar.accept(this);

        // forStat for and to are type checked during syntax analysis

        // The language specification specifies that forStat should NOT declare a new scope
        forStat.body.definesScope = false;
        forStat.body.accept(this);
        return null;
    }

    @Override
    public Void visitReadStat(ReadStat readStat) throws SemanticException {
        for(Identifier id: readStat.idList) {
            id.accept(this);
        }
        return null;
    }

    @Override
    public Void visitWriteStat(WriteStat writeStat) throws SemanticException {
        for(Expr expr: writeStat.exprList) {
            expr.accept(this);
        }
        return null;
    }

    @Override
    public Void visitAssignStat(AssignStat assignStat) throws SemanticException {
        if(assignStat.exprList.size() != assignStat.idList.size()) {
            throw new SemanticException("Wrong number of identifiers and expressions in assign statement");
        }
        Iterator<Identifier> idsIter = assignStat.idList.iterator();
        Iterator<Expr> exprIter = assignStat.exprList.iterator();
        while(idsIter.hasNext() && exprIter.hasNext()) {
            Identifier id = idsIter.next();
            Expr expr = exprIter.next();

            id.accept(this);
            expr.accept(this);
            if(!id.getType().equals(expr.getType())) {
                throw new SemanticException("Type mismatch");
            }
        }

        return null;
    }

    @Override
    public Void visitFunCall(FunCall funCall) throws SemanticException {
        this.typeEnv.setScope(funCall);
        ScopeEntry scopeEntry = this.typeEnv.lookup(funCall.id, ScopeEntry.SymbolType.FUNCTION);
        if (scopeEntry == null) {
            throw new SemanticException("Undeclared function");
        }
        Collection<Object> funParams = scopeEntry.extraData.values();
        if (funParams.size() != funCall.exprList.size()) {
            throw new SemanticException("Wrong amount of arguments for function");
        }

        Iterator<Object> funParamsIter = funParams.iterator();
        Iterator<Expr> exprIter = funCall.exprList.iterator();

        int i = 0;
        while (funParamsIter.hasNext() && exprIter.hasNext()) {
            ParDeclInfo parDeclInfo = (ParDeclInfo) funParamsIter.next();
            Expr expr = exprIter.next();
            expr.accept(this);


            if (parDeclInfo.isOut && !(expr instanceof Identifier)) {
                throw new SemanticException("Out parameters must be identifiers");
            }
            if (!parDeclInfo.type.equals(expr.getType())) {
                throw new SemanticException("Wrong type for positional argument #%d".formatted(i));
            }
            i++;
        }

        funCall.setType(scopeEntry.type);

        return null;
    }

    @Override
    public Void visitIdentifier(Identifier identifier) throws SemanticException {
        // Type check
        ScopeEntry scopeEntry = this.typeEnv.lookup(identifier);
        if (scopeEntry == null) {
            // This will be catched while processing varDecls, so that it can be re-attempted later
            throw new SemanticException("Undeclared identifier: " + identifier.attribute, SemanticException.Kind.Scope);
        }
        this.typeEnv.setScope(identifier);
        identifier.setType(scopeEntry.type);
        return null;
    }

    @Override
    public Void visitAddExpr(AddExpr addExpr) throws SemanticException {
        addExpr.first.accept(this);
        addExpr.second.accept(this);

        addExpr.setType(this.typeEnv.opType2(addExpr));
        return null;
    }

    @Override
    public Void visitSubExpr(SubExpr subExpr) throws SemanticException {
        subExpr.first.accept(this);
        subExpr.second.accept(this);

        subExpr.setType(this.typeEnv.opType2(subExpr));
        return null;
    }

    @Override
    public Void visitTimesExpr(TimesExpr timesExpr) throws SemanticException {
        timesExpr.first.accept(this);
        timesExpr.second.accept(this);

        timesExpr.setType(this.typeEnv.opType2(timesExpr));
        return null;
    }

    @Override
    public Void visitDivExpr(DivExpr divExpr) throws SemanticException {
        divExpr.first.accept(this);
        divExpr.second.accept(this);

        divExpr.setType(this.typeEnv.opType2(divExpr));
        return null;
    }

    @Override
    public Void visitAndExpr(AndExpr andExpr) throws SemanticException {
        andExpr.first.accept(this);
        andExpr.second.accept(this);

        andExpr.setType(this.typeEnv.opType2(andExpr));
        return null;
    }

    @Override
    public Void visitPowExpr(PowExpr powExpr) throws SemanticException {
        powExpr.first.accept(this);
        powExpr.second.accept(this);

        powExpr.setType(this.typeEnv.opType2(powExpr));
        return null;
    }

    @Override
    public Void visitStrConcatExpr(StrConcatExpr strConcatExpr) throws SemanticException {
        strConcatExpr.first.accept(this);
        strConcatExpr.second.accept(this);

        strConcatExpr.setType(this.typeEnv.opType2(strConcatExpr));
        return null;
    }

    @Override
    public Void visitOrExpr(OrExpr orExpr) throws SemanticException {
        orExpr.first.accept(this);
        orExpr.second.accept(this);

        orExpr.setType(this.typeEnv.opType2(orExpr));
        return null;
    }

    @Override
    public Void visitGTExpr(GTExpr gtExpr) throws SemanticException {
        gtExpr.first.accept(this);
        gtExpr.second.accept(this);

        gtExpr.setType(this.typeEnv.opType2(gtExpr));
        return null;
    }

    @Override
    public Void visitGEExpr(GEExpr geExpr) throws SemanticException {
        geExpr.first.accept(this);
        geExpr.second.accept(this);

        geExpr.setType(this.typeEnv.opType2(geExpr));
        return null;
    }

    @Override
    public Void visitLTExpr(LTExpr ltExpr) throws SemanticException {
        ltExpr.first.accept(this);
        ltExpr.second.accept(this);

        ltExpr.setType(this.typeEnv.opType2(ltExpr));
        return null;
    }

    @Override
    public Void visitLEExpr(LEExpr leExpr) throws SemanticException {
        leExpr.first.accept(this);
        leExpr.second.accept(this);

        leExpr.setType(this.typeEnv.opType2(leExpr));
        return null;
    }

    @Override
    public Void visitEQExpr(EQExpr eqExpr) throws SemanticException {
        eqExpr.first.accept(this);
        eqExpr.second.accept(this);

        eqExpr.setType(this.typeEnv.opType2(eqExpr));
        return null;
    }

    @Override
    public Void visitNEExpr(NEExpr neExpr) throws SemanticException {
        neExpr.first.accept(this);
        neExpr.second.accept(this);

        neExpr.setType(this.typeEnv.opType2(neExpr));
        return null;
    }

    @Override
    public Void visitMinusExpr(MinusExpr minusExpr) throws SemanticException {
        minusExpr.expr.accept(this);
        String type = this.typeEnv.opType1(minusExpr);
        minusExpr.setType(type);
        return null;
    }

    @Override
    public Void visitNotExpr(NotExpr notExpr) throws SemanticException {
        notExpr.expr.accept(this);
        String type = this.typeEnv.opType1(notExpr);
        notExpr.setType(type);
        return null;
    }

    // Handled directly on the node
    @Override
    public Void visitFalseConstExpr(FalseConstExpr falseConstExpr) {
        falseConstExpr.setType(Type.BOOL);
        return null;
    }

    @Override
    public Void visitParDecl(ParDecl parDecl) throws SemanticException {
        for (Identifier id : parDecl.idList) {
            HashMap<String, Object> extraData = new HashMap<>();
            extraData.put("isOut", parDecl.isOut);
            this.typeEnv.addEntry(id, ScopeEntry.SymbolType.PARAMETER, parDecl.type.type, extraData);

            id.accept(this);
        }
        return null;
    }

    @Override
    public Void visitReturnStat(ReturnStat returnStat) throws SemanticException {
        ScopeEntry scopeEntry = this.typeEnv.lookup(new Identifier("return"), ScopeEntry.SymbolType.RETURN);
        if (returnStat.returnVal != null) {
            //if(!returnStat.returnVal.getType().equals(scopeEntry.type)) {
            returnStat.returnVal.accept(this);
            Cast cast = this.typeEnv.widen(returnStat.returnVal, scopeEntry.type);
            if(cast != null) {
                returnStat.returnVal = cast;
            }

        } else {
            if(!scopeEntry.type.equals(Type.VOID)) {
                throw new SemanticException("Type mismatch");
            }
        }

        return null;
    }

    @Override
    public Void visitCast(Cast cast) throws SemanticException {
        // Not needed since the Cast node is created during semantic analysis and the inner expr has already been visited
        //cast.expr.accept(this);
        return null;
    }
}
