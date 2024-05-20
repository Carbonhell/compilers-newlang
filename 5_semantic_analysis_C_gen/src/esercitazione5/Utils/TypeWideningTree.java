package esercitazione5.Utils;

import esercitazione5.SyntaxTreeNodes.Type;

import java.util.LinkedList;

public class TypeWideningTree {
    public TypeWideningNode root;

    /**
     *       string
     *     /        \
     *   char       float
     *              |
     *              int
     */
    public TypeWideningTree() {
        this.root = new TypeWideningNode(Type.STRING);
        this.root.add(new TypeWideningNode(Type.CHAR));

        TypeWideningNode tFloat = new TypeWideningNode(Type.FLOAT);
        this.root.add(tFloat);

        TypeWideningNode tInt = new TypeWideningNode(Type.INT);
        tFloat.add(tInt);
    }

    public String findCommonAncestor(String first, String second) throws SemanticException {
        int treeHeight = getHeight(this.root);
        for(int i=0;i<treeHeight;i++) {
            TypeWideningNode ancestor = findCommonAncestorAtLevel(this.root, first, second, i);
            if(ancestor != null) {
                String child = ancestor.type.equals(first) ? second : first;
                if(hasChild(ancestor, child)) {
                    return ancestor.type;
                }
            }
        }
        throw new SemanticException("Type mismatch: cannot find common ancestor between " + first + " and " + second);
    }

    public boolean canWiden(String from, String to) {
        int treeHeight = getHeight(this.root);
        for(int i=0;i<treeHeight;i++) {
            TypeWideningNode ancestor = findTypeAtLevel(this.root, to, i);
            if(ancestor != null) {
                if(hasChild(ancestor, from)) {
                    return true;
                }
            }
        }
        return false;
    }

    private TypeWideningNode findCommonAncestorAtLevel(TypeWideningNode node, String first, String second, int level) {
        if(level == 0) {
            if(node.type.equals(first) || node.type.equals(second)) {
                return node;
            }
            return null;
        }
        for(TypeWideningNode child: node.children) {
            TypeWideningNode ancestor = findCommonAncestorAtLevel(child, first, second, level - 1);
            if(ancestor != null) {
                return ancestor;
            }
        }
        return null;
    }

    private TypeWideningNode findTypeAtLevel(TypeWideningNode node, String type, int level) {
        if(level == 0) {
            if(node.type.equals(type)) {
                return node;
            }
            return null;
        }
        for(TypeWideningNode child: node.children) {
            TypeWideningNode ancestor = findTypeAtLevel(child, type, level - 1);
            if(ancestor != null) {
                return ancestor;
            }
        }
        return null;
    }

    public boolean hasChild(TypeWideningNode node, String type) {
        if(node.type.equals(type)) {
            return true;
        }
        for(TypeWideningNode subNode: node.children) {
            if(hasChild(subNode, type)) {
                return true;
            }
        }
        return false;
    }

    private int getHeight(TypeWideningNode node) {
        if(node.children.isEmpty()) {
            return 0;
        }
        int maxSubHeight = 0;
        for(TypeWideningNode child: node.children) {
            int subHeight = getHeight(child);
            if(subHeight > maxSubHeight) {
                maxSubHeight = subHeight;
            }
        }
        return maxSubHeight+1;
    }
    static class TypeWideningNode {
        public String type;
        LinkedList<TypeWideningNode> children;

        public TypeWideningNode(String type) {
            this.type = type;
            this.children = new LinkedList<>();
        }

        public void add(TypeWideningNode node) {
            this.children.add(node);
        }
    }
}
