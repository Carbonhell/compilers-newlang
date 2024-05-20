import java_cup.runtime.Symbol;

public class Token {
    public static final int EOF=0;
    public static final int T_ERROR = 1;
    public static final int T_IF = 2;
    public static final int T_THEN = 3;
    public static final int T_ELSE = 4;
    public static final int T_WHILE = 5;
    public static final int T_INT = 6;
    public static final int T_FLOAT = 7;
    public static final int T_BOOL = 8;
    public static final int T_RETURN = 9;
    public static final int T_LCURLYBRACKET = 10;
    public static final int T_RCURLYBRACKET = 11;
    public static final int T_LBRACKET = 12;
    public static final int T_RBRACKET = 13;
    public static final int T_SEMICOLON = 14;
    public static final int T_COMMA = 15;
    public static final int T_NUMBER = 16;
    public static final int T_PLUS = 17;
    public static final int T_MINUS = 18;
    public static final int T_LESSOREQUAL = 19;
    public static final int T_NOTEQUAL = 20;
    public static final int T_LESS = 21;
    public static final int T_GREATEROREQUAL = 22;
    public static final int T_GREATER = 23;
    public static final int T_EQUAL = 24;
    public static final int T_ASSIGN = 25;
    public static final int T_ID = 26;
    public static final int T_STRLITERAL = 27;
    public static final int T_STR = 28;

    // Helper function to print out CUP symbols for debug purposes
    public static String symbolToString(Symbol s) {
        switch(s.sym) {
            case EOF:
                break;
            case T_ERROR:
                return "<ERROR>";
            case T_IF:
                return "<IF>";
            case T_THEN:
                return "<THEN>";
            case T_ELSE:
                return "<ELSE>";
            case T_WHILE:
                return "<WHILE>";
            case T_INT:
                return "<INT>";
            case T_FLOAT:
                return "<FLOAT>";
            case T_BOOL:
                return "<BOOL>";
            case T_RETURN:
                return "<RETURN>";
            case T_LCURLYBRACKET:
                return "<LCURLYBRACKET>";
            case T_RCURLYBRACKET:
                return "<RCURLYBRACKET>";
            case T_LBRACKET:
                return "<LBRACKET>";
            case T_RBRACKET:
                return "<RBRACKET>";
            case T_SEMICOLON:
                return "<SEMICOLON>";
            case T_COMMA:
                return "<COMMA>";
            case T_NUMBER:
                return "<NUMBER, " + s.value + ">";
            case T_PLUS:
                return "<PLUS>";
            case T_MINUS:
                return "<MINUS>";
            case T_LESSOREQUAL:
                return "<LESSOREQUAL>";
            case T_NOTEQUAL:
                return "<NOTEQUAL>";
            case T_LESS:
                return "<LESS>";
            case T_GREATEROREQUAL:
                return "<GREATEROREQUAL>";
            case T_GREATER:
                return "<GREATER>";
            case T_EQUAL:
                return "<EQUAL>";
            case T_ASSIGN:
                return "<ASSIGN>";
            case T_ID:
                return "<ID, \"" + s.value + "\">";
            case T_STRLITERAL:
                return "<STRINGLITERAL, \"" + s.value + "\">";
            case T_STR:
                return "<STR>";
        }
        return "";

    }
}
