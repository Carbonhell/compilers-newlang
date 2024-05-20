package esercitazione5.SyntaxTreeNodes.Expressions;

import esercitazione5.SyntaxTreeNodes.Type;

public class Const<T extends Expr> {
    public Type type;
    public T attribute;

    public Const(T attribute, Type type) {
        this.type = type;
        this.attribute = attribute;
    }
}
