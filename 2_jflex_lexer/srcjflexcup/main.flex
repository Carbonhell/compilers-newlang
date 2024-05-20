// main.lex
//
// Description of a basic lexer
// Recognizes keywords, relational operators (and assign), plus, minus, whitespace, identifiers and numbers
//
// Carbonhell

import java_cup.runtime.Symbol; 		//This is how we pass tokens to the parser
import java.util.HashMap;


%%

%class Lexer
%unicode
%cupsym Token
%cup
%line
%column

%{
    StringBuffer string = new StringBuffer();
    static HashMap<String, Symbol> reservedKeywords;
    public static HashMap<String, Symbol> stringTable;

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol installID(String lexeme) {
        if (reservedKeywords.containsKey(lexeme)) {
            return reservedKeywords.get(lexeme);
        } else {
            return stringTable.computeIfAbsent(lexeme, (k) -> symbol(Token.T_ID, lexeme));
        }
    }

%}

%init{
    stringTable = new HashMap<>();
    reservedKeywords = new HashMap<>();
    reservedKeywords.put("if", symbol(Token.T_IF));
    reservedKeywords.put("then", symbol(Token.T_THEN));
    reservedKeywords.put("else", symbol(Token.T_ELSE));
    reservedKeywords.put("while", symbol(Token.T_WHILE));
    reservedKeywords.put("int", symbol(Token.T_INT));
    reservedKeywords.put("float", symbol(Token.T_FLOAT));
    reservedKeywords.put("bool", symbol(Token.T_BOOL));
    reservedKeywords.put("return", symbol(Token.T_RETURN));
    reservedKeywords.put("str", symbol(Token.T_STR));

%init}

whitespace = [ \r\n\t\f]
id = [:jletter:] [:jletterdigit:]*
integer = 0 | [1-9][0-9]*
decimal = [0-9]*[1-9]
/* integers, integers with a dot and no decimals (defaults to .0) and floats */
number = {integer}[.]?{decimal}?
lineTerminator = \r|\n|\r\n
lineComment = "//" [^\n\r]* {lineTerminator}?
blockComment = "/*" ~"*/"
comment = {lineComment} | {blockComment}

%state stringLiteral
%%

<YYINITIAL> {
    {whitespace} | {comment}  {/* ignore */}
    "{" 		{ return symbol(Token.T_LCURLYBRACKET); }
    "}" 		{ return symbol(Token.T_RCURLYBRACKET); }
    "(" 		{ return symbol(Token.T_LBRACKET); }
    ")" 		{ return symbol(Token.T_RBRACKET); }
    ";" 		{ return symbol(Token.T_SEMICOLON); }
    "," 		{ return symbol(Token.T_COMMA); }
    "+" 		{ return symbol(Token.T_PLUS); }
    "-" 		{ return symbol(Token.T_MINUS); }
    "<=" 		{ return symbol(Token.T_LESSOREQUAL); }
    "<>" 		{ return symbol(Token.T_NOTEQUAL); }
    "<" 		{ return symbol(Token.T_LESS); }
    ">=" 		{ return symbol(Token.T_GREATEROREQUAL); }
    ">" 		{ return symbol(Token.T_GREATER); }
    "==" 		{ return symbol(Token.T_EQUAL); }
    "<--" 		{ return symbol(Token.T_ASSIGN); }
    \"          { string.setLength(0); yybegin(stringLiteral); }
    {id}        { return installID(yytext()); }
    {number}    {
                    String lexeme = yytext().endsWith(".") ? yytext() + "0" : yytext(); // Adds a 0 if the lexeme ends with a dot
                    return symbol(Token.T_NUMBER, lexeme);
                }
}

<stringLiteral> {
    \"      {
                yybegin(YYINITIAL);
                return symbol(Token.T_STRLITERAL, string.toString());
            }
    [^\"]+ {
                string.append(yytext());
            }
    <<EOF>> {
            yybegin(YYINITIAL);
            return symbol(Token.T_ERROR);
            }
}

/* Fallback */
[^]         { return symbol(Token.T_ERROR); }
