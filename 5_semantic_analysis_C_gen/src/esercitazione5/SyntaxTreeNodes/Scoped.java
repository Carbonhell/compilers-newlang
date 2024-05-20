package esercitazione5.SyntaxTreeNodes;

import esercitazione5.Utils.Scope;

public interface Scoped {
    Scope getScope();
    void setScope(Scope scope);
}
