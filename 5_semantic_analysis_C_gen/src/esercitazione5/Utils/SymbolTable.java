package esercitazione5.Utils;

import esercitazione5.SyntaxTreeNodes.Expressions.*;
import esercitazione5.SyntaxTreeNodes.Scoped;
import esercitazione5.SyntaxTreeNodes.Type;

import java.util.*;

public class SymbolTable {
    public ArrayDeque<Scope> scopes;
    // Used to properly order variable declarations.
    public Map<Identifier, Expr> fRefs;

    public SymbolTable() {
        this.scopes = new ArrayDeque<>();
        this.fRefs = new HashMap<>();
    }

    public void setScope(Scoped scoped) {
        if(scoped.getScope() == null) {
            scoped.setScope(this.scopes.peek());
        }
    }
    public void enterScope(Scoped scoped) {
        Scope scope;
        Scope currentScope = this.scopes.peek();
        if(scoped.getScope() == null) {
            scope = new Scope();
            scoped.setScope(scope);
        } else {
            scope = scoped.getScope();
        }
        scope.previousScope = currentScope;
        this.scopes.push(scope);
    }

    public void exitScope() {
        this.scopes.pop();
    }

    public boolean probe(Identifier id, ScopeEntry.SymbolType kind) {
        return this.strictLookup(id, kind) != null;
    }

    public boolean probe(Identifier id) {
        return this.strictLookup(id) != null;
    }

    public void addEntry(Identifier id, ScopeEntry.SymbolType kind, String type) throws SemanticException {
        if(this.probe(id, kind)) {
            throw new SemanticException("Identifier already declared: " + id.attribute);
        }

        Scope currentScope = this.scopes.peek();
        currentScope.addEntry(id, kind, type);
    }

    public void addEntry(Identifier id, ScopeEntry.SymbolType kind, String type, Map<String, Object> extraData) throws SemanticException {
        if(this.probe(id, kind)) {
            throw new SemanticException("Identifier already declared: " + id.attribute);
        }

        Scope currentScope = this.scopes.peek();
        currentScope.addEntry(id, kind, type, extraData);
    }

    public ScopeEntry lookup(Identifier id, ScopeEntry.SymbolType kind) {
        Iterator<Scope> scopesIterator = this.scopes.descendingIterator();
        while (scopesIterator.hasNext()) {
            Scope nextScope = scopesIterator.next();
            ScopeEntry entry = nextScope.strictLookup(id, kind);
            if(entry != null) {
                return entry;
            }
        }
        return null;
    }

    public ScopeEntry strictLookup(Identifier id, ScopeEntry.SymbolType kind) {
        Scope currentScope = this.scopes.peek();
        return currentScope.strictLookup(id, kind);
    }

    public ScopeEntry strictLookup(Identifier id) {
        Scope currentScope = this.scopes.peek();
        return currentScope.strictLookup(id);
    }

    public ScopeEntry lookup(Identifier id) {
        Iterator<Scope> scopesIterator = this.scopes.descendingIterator();
        while (scopesIterator.hasNext()) {
            Scope nextScope = scopesIterator.next();
            ScopeEntry entry = nextScope.strictLookup(id);
            if(entry != null) {
                return entry;
            }
        }
        return null;
    }

    public void addFRef(Identifier id, Expr expr) {
        this.fRefs.put(id, expr);
    }

    // Adds the necessary casts based on the common type ancestor between first and second.
    public Cast widen(Expr first, Expr second) throws SemanticException {
        String commonType = this.commonType(first.getType(), second.getType());
        if(commonType == null) {
            return null;
        }
        Cast cast = new Cast(first.getType().equals(commonType) ? second : first);
        cast.setType(commonType);
        return cast;
    }

    public Cast widen(Expr victim, String targetType) throws SemanticException {
        if(victim.getType().equals(targetType)) {
            return null;
        }
        TypeWideningTree typeWideningTree = new TypeWideningTree();
        if(typeWideningTree.canWiden(victim.getType(), targetType)) {
            Cast cast = new Cast(victim);
            cast.setType(targetType);
            return cast;
        }
        throw new SemanticException("Type mismatch: cannot cast to " + targetType);
    }

    private String commonType(String first, String second) throws SemanticException {
        if(first.equals(second)) {
            return null;
        }
        TypeWideningTree typeWideningTree = new TypeWideningTree();
        return typeWideningTree.findCommonAncestor(first, second);
    }

    // Check if the current scope includes identifiers marked as forward references.
    // If that's the case, process them.
    public ArrayList<Expr> processFRefs() {
        ArrayList<Identifier> toRemove = new ArrayList<>();
        ArrayList<Expr> processableExprs = new ArrayList<>();
        for(Map.Entry<Identifier, Expr> entry: this.fRefs.entrySet()) {
            Identifier id = entry.getKey();
            Expr expr = entry.getKey();
            // a previous var declaration uses an ID which has now been declared: fix the ordering
            if(this.probe(id)) {
                Scope currentScope = this.scopes.peek();
                currentScope.incrementIndex(id);
                toRemove.add(id);
                processableExprs.add(expr);
            }
        }
        for(Identifier id: toRemove) {
            this.fRefs.remove(id);
        }
        return processableExprs;
    }

    private static final Map<OpTypeMatrixEntry, String> opType1Matrix = Map.ofEntries(
            Map.entry(new OpTypeMatrixEntry("MinusExpr", Type.INT), Type.INT),
            Map.entry(new OpTypeMatrixEntry("MinusExpr", Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("NotExpr", Type.BOOL), Type.BOOL)
    );

    public <T extends UnaryExpr> String opType1(T node) throws SemanticException {
        String op = node.getClass().getSimpleName();
        String type = opType1Matrix.get(new OpTypeMatrixEntry(op, node.expr.getType()));
        if (type == null) {
            throw new SemanticException("Type mismatch");
        }
        return type;
    }

    static class OpTypeMatrixEntry {
        public String op;
        public String firstType;
        public String secondType;

        public OpTypeMatrixEntry(String op, String firstType) {
            this.op = op;
            this.firstType = firstType;
        }

        public OpTypeMatrixEntry(String op, String firstType, String secondType) {
            this.op = op;
            this.firstType = firstType;
            this.secondType = secondType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OpTypeMatrixEntry that = (OpTypeMatrixEntry) o;
            return op.equals(that.op) && firstType.equals(that.firstType) && Objects.equals(secondType, that.secondType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(op, firstType, secondType);
        }
    }

    private static final Map<OpTypeMatrixEntry, String> opType2Matrix = Map.ofEntries(
            Map.entry(new OpTypeMatrixEntry("AddExpr", Type.INT, Type.INT), Type.INT),
            Map.entry(new OpTypeMatrixEntry("AddExpr", Type.INT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("AddExpr", Type.FLOAT, Type.INT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("AddExpr", Type.FLOAT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("SubExpr", Type.INT, Type.INT), Type.INT),
            Map.entry(new OpTypeMatrixEntry("SubExpr", Type.INT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("SubExpr", Type.FLOAT, Type.INT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("SubExpr", Type.FLOAT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("TimesExpr", Type.INT, Type.INT), Type.INT),
            Map.entry(new OpTypeMatrixEntry("TimesExpr", Type.INT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("TimesExpr", Type.FLOAT, Type.INT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("TimesExpr", Type.FLOAT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("DivExpr", Type.INT, Type.INT), Type.INT),
            Map.entry(new OpTypeMatrixEntry("DivExpr", Type.INT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("DivExpr", Type.FLOAT, Type.INT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("DivExpr", Type.FLOAT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("PowExpr", Type.INT, Type.INT), Type.INT),
            Map.entry(new OpTypeMatrixEntry("PowExpr", Type.INT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("PowExpr", Type.FLOAT, Type.INT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("PowExpr", Type.FLOAT, Type.FLOAT), Type.FLOAT),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.STRING, Type.STRING), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.STRING, Type.INT), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.INT, Type.STRING), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.INT, Type.INT), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.STRING, Type.FLOAT), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.FLOAT, Type.STRING), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.FLOAT, Type.FLOAT), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.STRING, Type.CHAR), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.CHAR, Type.STRING), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.CHAR, Type.CHAR), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.STRING, Type.BOOL), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.BOOL, Type.STRING), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("StrConcatExpr", Type.BOOL, Type.BOOL), Type.STRING),
            Map.entry(new OpTypeMatrixEntry("AndExpr", Type.BOOL, Type.BOOL), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("OrExpr", Type.BOOL, Type.BOOL), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GTExpr", Type.INT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GTExpr", Type.INT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GTExpr", Type.FLOAT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GTExpr", Type.FLOAT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GEExpr", Type.INT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GEExpr", Type.INT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GEExpr", Type.FLOAT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("GEExpr", Type.FLOAT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LTExpr", Type.INT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LTExpr", Type.INT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LTExpr", Type.FLOAT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LTExpr", Type.FLOAT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LEExpr", Type.INT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LEExpr", Type.INT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LEExpr", Type.FLOAT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("LEExpr", Type.FLOAT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("EQExpr", Type.INT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("EQExpr", Type.INT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("EQExpr", Type.FLOAT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("EQExpr", Type.FLOAT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("EQExpr", Type.STRING, Type.STRING), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("NEExpr", Type.INT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("NEExpr", Type.INT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("NEExpr", Type.FLOAT, Type.INT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("NEExpr", Type.FLOAT, Type.FLOAT), Type.BOOL),
            Map.entry(new OpTypeMatrixEntry("NEExpr", Type.STRING, Type.STRING), Type.BOOL)
    );

    public <T extends BinaryExpr> String opType2(T node) throws SemanticException {
        String op = node.getClass().getSimpleName();
        String type = opType2Matrix.get(new OpTypeMatrixEntry(op, node.first.getType(), node.second.getType()));
        if (type == null) {
            throw new SemanticException("Type mismatch");
        }
        return type;
    }
}
