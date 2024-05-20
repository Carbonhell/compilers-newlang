package esercitazione4.SyntaxTreeNodes;

import esercitazione4.Visitor.Visitor;

import java.util.Objects;

/**
 * Represents Type and TypeOrVoid.
 */
public class Type implements Visitable{
    public final static String INT = "integer";
    public final static String BOOL = "boolean";
    public final static String FLOAT = "real";
    public final static String STRING = "string";
    public final static String CHAR = "char";
    public final static String VOID = "void"; // TODO needed?


    public String type;

    public Type(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type1 = (Type) o;
        return type.equals(type1.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitType(this);
    }
}
