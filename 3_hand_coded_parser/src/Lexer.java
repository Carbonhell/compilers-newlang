import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Lexer {

    private BufferedReader input;
    private static HashMap<String, Token> reservedKeywords;
    private static HashMap<String, Token> stringTable;
    private State state;
    private static int line = 1;

    public enum State {
        START,
        LETTER, // identifier or keyword
        NUMBER,
        FLOATSTART, // number with dot
        FLOAT,
        LESS,
        GREATER,
        EQUAL,
        ARROW_START,
        COMMENT
    }

    public Lexer() {
        stringTable = new HashMap<>();
        reservedKeywords = new HashMap<>();
        state = State.START;
        reservedKeywords.put("if", new Token(Token.T_IF));
        reservedKeywords.put("then", new Token(Token.T_THEN));
        reservedKeywords.put("else", new Token(Token.T_ELSE));
        reservedKeywords.put("while", new Token(Token.T_WHILE));
        reservedKeywords.put("int", new Token(Token.T_INT));
        reservedKeywords.put("float", new Token(Token.T_FLOAT));
        reservedKeywords.put("bool", new Token(Token.T_BOOL));
        reservedKeywords.put("return", new Token(Token.T_RETURN));
        reservedKeywords.put("loop", new Token(Token.T_LOOP));
        reservedKeywords.put("end", new Token(Token.T_END));


    }

    /**
     * @param filePath path of the file.
     * @return false if the file couldn't be found, otherwise true.
     */
    public Boolean initialize(String filePath) {
        try {
            input = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filePath),
                            StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;

    }

    public Token nextToken() throws IOException {
        state = State.START;
        StringBuilder lexeme = new StringBuilder();

        while (true) {

            int inputChar = input.read();
            char c = (inputChar == -1) ? ' ' : (char) inputChar; // If EOF is detected, use an empty space to get the last token processed.

            switch (state) {
                case START: // lexeme is empty
                    if (c == '#') {
                        state = State.COMMENT;
                        break;
                    }
                    if (inputChar != -1 && Character.isWhitespace(c)) {
                        if (c == '\n') {
                            line++;
                        }
                        continue;
                    } else if (Character.isLetter(c)) {
                        lexeme.append(c);
                        state = State.LETTER;
                        break;
                    } else if (Character.isDigit(c)) {
                        lexeme.append(c);
                        state = State.NUMBER;
                        break;
                    }
                    // Separators and operators
                    switch (c) {
                        case '{':
                            return new Token(Token.T_LCURLYBRACKET, line);
                        case '}':
                            return new Token(Token.T_RCURLYBRACKET, line);
                        case '(':
                            return new Token(Token.T_LBRACKET, line);
                        case ')':
                            return new Token(Token.T_RBRACKET, line);
                        case ';':
                            return new Token(Token.T_SEMICOLON, line);
                        case ',':
                            return new Token(Token.T_COMMA, line);
                        case '<':
                            lexeme.append(c);
                            state = State.LESS;
                            break;
                        case '>':
                            lexeme.append(c);
                            state = State.GREATER;
                            break;
                        case '=':
                            lexeme.append(c);
                            state = State.EQUAL;
                            break;
                        case '+':
                            return new Token(Token.T_PLUS, line);
                        case '-':
                            return new Token(Token.T_MINUS, line);
                        default:
                            if (inputChar != -1) {
                                lexeme.append(c);
                                return new Token(Token.T_ERROR, lexeme.toString(), line);
                            }
                    }
                    break;

                case COMMENT:
                    if (c == '\n') {
                        line++;
                        state = State.START;
                        break;
                    }
                    break;
                case LETTER:
                    if (Character.isLetterOrDigit(c)) {
                        lexeme.append(c);
                        break;
                    } else {
                        input.reset();
                        // case sensitive!
                        // if the lexeme is a reserved keyword, return the associated token
                        // else return the token associated to the identifier and create it if it doesn't exist
                        return installID(lexeme.toString(), line);
                    }

                case NUMBER:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                    } else if (c == '.') {
                        lexeme.append(c);
                        state = State.FLOATSTART;
                    } else {
                        input.reset();
                        return new Token(Token.T_NUMBER, lexeme.toString(), line);
                    }
                    break;

                // 2.a? 2. is accepted, by converting it to 2.0
                case FLOATSTART:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                        state = State.FLOAT;
                    } else {
                        lexeme.append('0');
                        input.reset();
                        return new Token(Token.T_NUMBER, lexeme.toString(), line);
                    }
                    break;

                case FLOAT:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                    } else {
                        input.reset();
                        return new Token(Token.T_NUMBER, lexeme.toString(), line);
                    }
                    break;

                case LESS:
                    if (c == '=') {
                        return new Token(Token.T_LESSOREQUAL, line);
                    } else if (c == '-') {
                        lexeme.append(c);
                        state = State.ARROW_START;
                        break;
                    } else if (c == '>') {
                        return new Token(Token.T_NOTEQUAL, line);
                    } else {
                        input.reset();
                        return new Token(Token.T_LESS, line);
                    }

                case GREATER:
                    if (c == '=') {
                        return new Token(Token.T_GREATEROREQUAL, line);
                    } else {
                        input.reset();
                        return new Token(Token.T_GREATER, line);
                    }

                case EQUAL:
                    if (c == '=') {
                        return new Token(Token.T_EQUAL, line);
                    } else {
                        input.reset();
                        return new Token(Token.T_ERROR, lexeme.toString(), line);
                    }

                case ARROW_START:
                    if (c == '-') {
                        return new Token(Token.T_ASSIGN, line);
                    } else {
                        input.reset();
                        return new Token(Token.T_ERROR, lexeme.toString(), line);
                    }
            }
            // Checked at the end to let the last lexeme get processed
            if (inputChar == -1) {
                return new Token(Token.T_EOF, line);
            }
            input.mark(1); // So that we can retract if the current token ends in the next character
        }
    }

    private Token installID(String lexeme, int line) {
        if (reservedKeywords.containsKey(lexeme)) {
            Token reservedToken = reservedKeywords.get(lexeme).clone();
            reservedToken.setLine(line);
            return reservedToken;
        } else {
            return stringTable.computeIfAbsent(lexeme, (k) -> new Token(Token.T_ID, lexeme, line));
        }
    }

}
