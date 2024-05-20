
public class Token implements Cloneable {
    // Token names
    public final static String T_ERROR = "ERROR";
    public final static String T_IF = "IF";
    public final static String T_THEN = "THEN";
    public final static String T_ELSE = "ELSE";
    public final static String T_WHILE = "WHILE";
    public final static String T_LOOP = "LOOP";

    public final static String T_END = "END";
    public final static String T_INT = "INT";
    public final static String T_FLOAT = "FLOAT";
    public final static String T_BOOL = "BOOL";
    public final static String T_RETURN = "RETURN";
    public final static String T_LCURLYBRACKET = "LCURLYBRACKET";
    public final static String T_RCURLYBRACKET = "RCURLYBRACKET";
    public final static String T_LBRACKET = "LBRACKET";
    public final static String T_RBRACKET = "RBRACKET";
    public final static String T_SEMICOLON = "SEMICOLON";
    public final static String T_COMMA = "COMMA";
    public final static String T_NUMBER = "NUMBER";
    public final static String T_PLUS = "PLUS";
    public final static String T_MINUS = "MINUS";
    public final static String T_LESSOREQUAL = "LESSOREQUAL";
    public final static String T_NOTEQUAL = "NOTEQUAL";
    public final static String T_LESS = "LESS";
    public final static String T_GREATEROREQUAL = "GREATEROREQUAL";
    public final static String T_GREATER = "GREATER";
    public final static String T_EQUAL = "EQUAL";
    public final static String T_ASSIGN = "ASSIGN";
    public final static String T_ID = "ID";
    public final static String T_EOF = "EOF";

    private String name;
    private String attribute;
    private int line;

    public Token(String name, String attribute, int line) {
        this.name = name;
        this.attribute = attribute;
        this.line = line;
    }

    public Token(String name, int line) {
        this.name = name;
        this.attribute = null;
        this.line = line;
    }

    // Used by reserved keywords
    public Token(String name) {
        this.name = name;
        this.attribute = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String toString() {
        return attribute == null ? "<" + name + ">" : "<" + name + ", \"" + attribute + "\">";
    }

    @Override
    protected Token clone() {
        try {
            return (Token) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
