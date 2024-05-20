# Lexical Analyzer

## How to run
Pass file paths as command arguments to analyze them.
Example: src.Tester "test_files/test_1.txt" "test_files/test_2.txt"

## Supported lexemes:
- Delimiters (ignored by the analyzer):
    - Unicode space characters
    - Tabulations
    - Feeds (Line, Form)
    - Separators (File, Group, Record, Unit)
    - Carriage return
- Reserved keywords:
    - if
    - then
    - else
    - while
    - int
    - float
- Identifiers: any word starting with a letter and continuing with letters or digits which isn't a reserved keyword
- Literals: Numbers and floating point
- Separators: Round and curly brackets, comma and semicolon
- Relational operators:
    - < Lesser than
    - \> Greater than
    - <= Lesser or equal than
    - \>= Greater or equal than
    - == Equal
    - <> Not equal
    - <-- Assignment operator
- Operators:
    - \+ Plus
    - \- Minus

## Lexer automata
- The presence of an asterisk (*) within a state's label means that the lexer will roll back to the previous character if it cannot match that character to any transition (retrack);
- Transitions without a symbol apply on the symbol shown by the name of the state they're pointing to;
- States without transitions are end states, which cause the lexer to return a Token;

### Definitions
- Whitespace symbol: space, newline, return carriage, tabulation and all the characters matched by Character.isWhitespace
- Digit: any symbol specifying a number, in any language: any symbol that matches Character.isDigit
- Letter: any symbol that matches Character.isLetter
- Default: any symbol not specified by other transitions on the same state

If the lexer stops on a final state, a new Token is returned with the name shown by the state, if it stops on a non final state, a new error token is returned.
If the lexer reads a character from a state with no transitions existing for that specific character, the automaton stops and returns the token associated to that state.
![](automata.svg)

## Regular definitions
- delimiter -> [ \n\t\r]
- delimiters -> delimiter*
- digit -> [0-9]
- digits -> digit*
- number -> digit digits [.]? digits
- letter -> [A-Za-z]
- letters -> letter*
- id -> letter (letter | digit)*
- if -> if
- then -> then
- else -> else
- while -> while
- int -> int
- float -> float
- lcurlybracket -> }
- rcurlybracket -> {
- lbracket -> \)
- rbracket -> \(
- semicolon -> ;
- comma -> ,
- plus -> \+
- minus -> \-
- lessorequal -> <=
- notequal -> <>
- less -> <
- greaterorequal -> >=
- greater -> >
- equal -> ==
- assign -> <--