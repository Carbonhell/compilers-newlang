package esercitazione5.Utils;

import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;

import java.util.ArrayList;
import java.util.Map;

public class Scope {
    public ArrayList<ScopeEntry> scopeEntries;

    public Scope previousScope;
    public int currentIndex = 0;

    public Scope() {
        this.scopeEntries = new ArrayList<>();
    }

    public ScopeEntry lookup(Identifier id, ScopeEntry.SymbolType kind) {
        Scope currentScope = this;
        while(currentScope != null) {
            ScopeEntry entry = currentScope.strictLookup(id, kind);
            if(entry != null) {
                return entry;
            }
            currentScope = currentScope.previousScope;
        }
        return null;
    }

    public ScopeEntry lookup(Identifier id) {
        Scope currentScope = this;
        while(currentScope != null) {
           ScopeEntry entry = currentScope.strictLookup(id);
           if(entry != null) {
               return entry;
           }
            currentScope = currentScope.previousScope;
        }
        return null;
    }

    public ScopeEntry strictLookup(Identifier id) {
        for (ScopeEntry entry : this.scopeEntries) {
            if (entry.lexeme.equals(id.attribute)) {
                return entry;
            }
        }
        return null;
    }

    public ScopeEntry strictLookup(Identifier id, ScopeEntry.SymbolType kind) {
        for (ScopeEntry entry : this.scopeEntries) {
            if(entry.lexeme.equals(id.attribute) && entry.kind == kind) {
                return entry;
            }
        }
        return null;
    }

    public void addEntry(Identifier id, ScopeEntry.SymbolType kind, String type) {
        ScopeEntry entry = new ScopeEntry(id, kind, type);
        this.scopeEntries.add(entry);
    }

    public void addEntry(Identifier id, ScopeEntry.SymbolType kind, String type, Map<String, Object> extraData) {
        ScopeEntry entry = new ScopeEntry(id, kind, type, extraData);
        this.scopeEntries.add(entry);
    }

    //
    public void incrementIndex(Identifier id) {
        ScopeEntry entry = this.strictLookup(id);
        currentIndex++;
        entry.order = currentIndex;
    }
}
