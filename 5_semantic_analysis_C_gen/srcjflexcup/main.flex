// main.lex
//
// NewLang lexer
//
// Carbonhell
package esercitazione5;

import java_cup.runtime.Symbol; 		//This is how we pass tokens to the parser
import java.util.HashMap;


%%

%class Lex
%public
%unicode
%cup
%line
%column

%{
    StringBuffer string = new StringBuffer(); // Used for string literals
    static HashMap<String, Integer> reservedKeywords;

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol installID(String lexeme) {
        if (reservedKeywords.containsKey(lexeme)) {
            return symbol(reservedKeywords.get(lexeme));
        } else {
            return symbol(sym.T_ID, lexeme);
        }
    }

%}


%init{
    reservedKeywords = new HashMap<>();
    reservedKeywords.put("var", sym.T_VAR);
    reservedKeywords.put("integer", sym.T_INT);
    reservedKeywords.put("float", sym.T_FLOAT);
    reservedKeywords.put("string", sym.T_STRING);
    reservedKeywords.put("boolean", sym.T_BOOL);
    reservedKeywords.put("char", sym.T_CHAR);
    reservedKeywords.put("void", sym.T_VOID);

    reservedKeywords.put("def", sym.T_DEF);
    reservedKeywords.put("out", sym.T_OUT);
    reservedKeywords.put("for", sym.T_FOR);
    reservedKeywords.put("if", sym.T_IF);
    reservedKeywords.put("then", sym.T_THEN);
    reservedKeywords.put("else", sym.T_ELSE);
    reservedKeywords.put("while", sym.T_WHILE);
    reservedKeywords.put("to", sym.T_TO);
    reservedKeywords.put("loop", sym.T_LOOP);
    reservedKeywords.put("return", sym.T_RETURN);

    reservedKeywords.put("true", sym.T_TRUE);
    reservedKeywords.put("false", sym.T_FALSE);
    reservedKeywords.put("and", sym.T_AND);
    reservedKeywords.put("or", sym.T_OR);
    reservedKeywords.put("not", sym.T_NOT);
%init}

whitespace = [ \r\n\t\f]
lineTerminator = \r|\n|\r\n
lineComment = "||" [^\n\r]* {lineTerminator}?
blockComment = "|*" ~"|"
comment = {lineComment} | {blockComment}
id = [$_A-Za-z] [$_A-Za-z0-9]*
integer = 0 | [1-9][0-9]*
decimal = [0-9]*[1-9]
/* integers with a dot and no decimals (defaults to .0) and floats */
real = {integer}[.]{decimal}?
char = ' [^'] ' /* TODO does this work with EOF? */

%state stringLiteral
%state charLiteral
%%

<YYINITIAL> {
    {whitespace} | {comment}    {/* ignore */}
    "start:"                    { return symbol(sym.T_MAIN); }
    ";"                         { return symbol(sym.T_SEMI); }
    ","                         { return symbol(sym.T_COMMA); }
    "|"                         { return symbol(sym.T_PIPE); }
    "<--"                       { return symbol(sym.T_READ); }
    "-->!"                      { return symbol(sym.T_WRITELN); }
    "-->"                       { return symbol(sym.T_WRITE); }
    "("                         { return symbol(sym.T_LPAR); }
    ")"                         { return symbol(sym.T_RPAR); }
    "{"                         { return symbol(sym.T_LBRAC); }
    "}"                         { return symbol(sym.T_RBRAC); }
    ":"                         { return symbol(sym.T_COLON); }
    "<<"                        { return symbol(sym.T_ASSIGN); }

    "+"                         { return symbol(sym.T_PLUS); }
    "-"                         { return symbol(sym.T_MINUS); }
    "*"                         { return symbol(sym.T_TIMES); }
    "/"                         { return symbol(sym.T_DIV); }
    "^"                         { return symbol(sym.T_POW); }
    "&"                         { return symbol(sym.T_STR_CONCAT); }
    "="                         { return symbol(sym.T_EQ); }
    "<>"|"!="                   { return symbol(sym.T_NE); }
    "<"                         { return symbol(sym.T_LT); }
    "<="                        { return symbol(sym.T_LE); }
    ">"                         { return symbol(sym.T_GT); }
    ">="                        { return symbol(sym.T_GE); }

    \"                          { string.setLength(0); yybegin(stringLiteral); }
    {char}                      { return symbol(sym.T_CHAR_CONST, yytext().substring(1,2)); }
    {real}                      {
                                    String lexeme = yytext().endsWith(".") ? yytext() + "0" : yytext(); // Adds a 0 if the lexeme ends with a dot
                                    return symbol(sym.T_REAL_CONST, lexeme);
                                }
    {integer}                   { return symbol(sym.T_INTEGER_CONST, yytext()); }
    {id}                        { return installID(yytext()); }
}

<stringLiteral> {
    \"      {
                yybegin(YYINITIAL);
                return symbol(sym.T_STRING_CONST, string.toString());
            }
    [^\"]+ {
                string.append(yytext());
            }
    <<EOF>> { throw new Error("\n\nStringa costante non completata"); }
}

/* Fallback */
[^]         { throw new Error("\n\nCarattere illegale < "+ yytext()+" >\n"); }

