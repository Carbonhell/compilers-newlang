package esercitazione5.SyntaxTreeNodes;

import esercitazione5.Utils.SemanticException;
import esercitazione5.Visitor.Visitor;

/**
 * Marks a node as a syntax tree node.
 */
public interface Visitable {
    <T> T accept(Visitor<T> v) throws SemanticException;
}
