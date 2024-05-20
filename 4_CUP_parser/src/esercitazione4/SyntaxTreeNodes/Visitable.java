package esercitazione4.SyntaxTreeNodes;

import esercitazione4.Visitor.Visitor;

/**
 * Marks a node as a syntax tree node.
 */
public interface Visitable {
    <T> T accept(Visitor<T> v);
}
