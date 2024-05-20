package esercitazione5.Utils;

public class SemanticException extends Exception {
    public Kind kind;
    public static enum Kind {
        Generic,
        Scope
    }
    public SemanticException(String message) {
        super(message);
        kind = Kind.Generic;
    }

    public SemanticException(String message, Kind kind) {
        super(message);
        this.kind = kind;
    }
}
