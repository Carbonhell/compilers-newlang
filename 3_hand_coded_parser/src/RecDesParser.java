import java.io.IOException;
import java.util.*;

public class RecDesParser {
    /*
     * Represents tokens fetched from the lexer but not yet parsed syntactically.
     * This is used for backtracking.
     */
    private final ArrayList<Token> buffer;
    private int lastIndex = 0;
    private final Lexer lexer;
    private String errorMessage;

    public static void main(String[] args) throws IOException {
        Lexer lexicalAnalyzer = new Lexer();
        RecDesParser parser = new RecDesParser(lexicalAnalyzer);
        for (String filePath : args) {
            if (lexicalAnalyzer.initialize(filePath)) {
                if (parser.S()) {
                    System.out.println("Input is valid");
                } else {
                    System.out.println("Syntax error");
                }
            } else
                System.out.println("File not found.");
        }
    }

    public RecDesParser(Lexer lexer) {
        buffer = new ArrayList<>();
        this.lexer = lexer;
    }

    // Initial non terminal
    private boolean S() throws IOException {
        if (!Program()) {
            return false;
        }
        Token t = nextToken();
        return t.getName().equals(Token.T_EOF);
    }

    private boolean Program() throws IOException {
        if (!Stmt()) {
            return false;
        }
        return Program1();
    }

    private boolean Program1() throws IOException {
        int fallback = lastIndex;
        Token t = nextToken();
        if (t.getName().equals(Token.T_SEMICOLON)) {
            if (Stmt()) {
                if (Program1()) {
                    return true;
                }
            }
        }
        // Backtrack to accept eps
        lastIndex = fallback;
        return true;
    }

    private boolean Stmt() throws IOException {
        Token t = nextToken();
        switch (t.getName()) {
            case Token.T_IF:
                if (!Expr()) {
                    return false;
                }
                Token t1 = nextToken();
                if (!t1.getName().equals(Token.T_THEN)) {
                    setErrorMessage(Token.T_THEN, t1);
                    return false;
                }
                if (!Stmt()) {
                    return false;
                }
                if(!ElseStmt()) {
                    return false;
                }
                Token t1a = nextToken();
                if (!t1a.getName().equals(Token.T_END)) {
                    return false;
                }
                Token t1b = nextToken();
                return t1b.getName().equals(Token.T_IF);
            case Token.T_ID:
                Token t2a = nextToken();
                if (!t2a.getName().equals(Token.T_ASSIGN)) {
                    setErrorMessage(Token.T_ASSIGN, t2a);
                    return false;
                }
                return Expr();
            case Token.T_WHILE:
                if(!Expr()) {
                    return false;
                }
                Token t3 = nextToken();
                if (!t3.getName().equals(Token.T_LOOP)) {
                    setErrorMessage(Token.T_LOOP, t3);
                    return false;
                }
                if (!Stmt()) {
                    return false;
                }
                Token t3a = nextToken();
                if (!t3a.getName().equals(Token.T_END)) {
                    setErrorMessage(Token.T_END, t3a);
                    return false;
                }
                Token t3b = nextToken();
                if (!t3b.getName().equals(Token.T_LOOP)) {
                    setErrorMessage(Token.T_LOOP, t3b);
                    return false;
                }
                return true;
        }
        setErrorMessage(Token.T_IF + ", " + Token.T_ID + " or " + Token.T_WHILE, t);
        return false;
    }

    private boolean ElseStmt() throws IOException {
        int fallback = lastIndex;
        Token t1 = nextToken();
        if (!t1.getName().equals(Token.T_ELSE)) {
            lastIndex = fallback;
            return true;
        }

        return Stmt();
    }

    private boolean Expr() throws IOException {
        if (!Expr1()) {
            return false;
        }
        return Expr2();
    }

    private boolean Expr1() throws IOException {
        Token t = nextToken();
        if (t.getName().equals(Token.T_ID)
                || t.getName().equals(Token.T_NUMBER)
                || t.getName().equals(Token.T_FLOAT)) {
            return true;
        } else {
            setErrorMessage(Token.T_ID + ", " + Token.T_NUMBER + " or " + Token.T_FLOAT, t);
            return false;
        }
    }

    private boolean Expr2() throws IOException {
        int fallback = lastIndex;
        if (Relop()) {
            if (Expr1()) {
                if (Expr2()) {
                    return true;
                }
            }
        }
        lastIndex = fallback;
        return true;
    }

    private boolean Relop() throws IOException {
        Token t = nextToken();
        String name = t.getName();
        if (name.equals(Token.T_LESS)
                || name.equals(Token.T_LESSOREQUAL)
                || name.equals(Token.T_GREATER)
                || name.equals(Token.T_GREATEROREQUAL)
                || name.equals(Token.T_EQUAL)
                || name.equals(Token.T_NOTEQUAL)) {
            return true;
        } else {
            setErrorMessage("relational operator", t);
            return false;
        }
    }


    // Utils methods
    private Token nextToken() throws IOException {
        if (lastIndex >= buffer.size()) {
            Token t = lexer.nextToken();
            buffer.add(t);
            lastIndex++;

            return t;
        }
        return buffer.get(lastIndex++);
    }

    private void setErrorMessage(String expected, Token found) {
        String s = "Expected " + expected + ", found " + found.getName();
        if (found.getAttribute() != null) {
            s += " (" + found.getAttribute() + ")";
        }
        s += " at line " + found.getLine();
        this.errorMessage = s;
    }
}