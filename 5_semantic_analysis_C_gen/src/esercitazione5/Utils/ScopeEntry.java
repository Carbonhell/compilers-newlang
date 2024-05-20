package esercitazione5.Utils;

import esercitazione5.SyntaxTreeNodes.Expressions.Identifier;

import java.util.Map;

public class ScopeEntry {
    public String lexeme;
    public SymbolType kind;
    public String type; // todo enum?
    public Map<String, Object> extraData;
    public int order = 0;

    public ScopeEntry(Identifier id, SymbolType kind, String type) {
        this.lexeme = id.attribute;
        this.kind = kind;
        this.type = type;
    }

    public ScopeEntry(Identifier id, SymbolType kind, String type, Map<String, Object> extraData) {
        this.lexeme = id.attribute;
        this.kind = kind;
        this.type = type;
        this.extraData = extraData;
    }

    public enum SymbolType {
        VAR,
        FUNCTION,
        PARAMETER,
        RETURN
    }
}
