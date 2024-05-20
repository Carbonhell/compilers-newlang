# Grammatica iniziale
(<br>
N = {S, Program, Stmt, Expr, Relop},<br>
T = {ID, IF, THEN, ELSE, END, LESS, GREATER, LESSOREQUAL, GREATEROREQUAL, EQUAL, NOTEQUAL, NUMBER, ;, ASSIGN, WHILE, LOOP, EOF},  <br>
S<br>
P = {<br>

          S -> Program EOF

          Program -> Program ; Stmt
                     |  Stmt
          Stmt -> IF Expr THEN Stmt END IF
                     | IF Expr THEN Stmt ELSE Stmt END IF
                     | ID ASSIGN Expr
                     | WHILE Epr LOOP Stmt END LOOP

          Expr ->  Expr Relop Expr

          Expr ->   ID
                     |  NUMBER

          Relop -> LESS
                    | LESSOREQUAL
                    | GREATER
                    | GREATEROREQUAL
                    | EQUAL
                    | NOTEQUAL
}<br>
)

## Analisi grammatica
### Ben formata
La grammatica è ben formata poiché:
1) Ogni non terminale è raggiungibile dal terminale iniziale S
2) Da ogni forma sentenziale si può raggiungere una sentenza
### Ambiguità
La grammatica è ambigua, in particolare lo è la produzione:

        Expr -> Expr Relop Expr
Usando una espressione del tipo:<br>
a < b < c<br>
Poiché possiamo procedere sia assegnando 'a < b' al primo Expr, oppure assegnando 'a' al primo Expr
e 'b < c' al secondo, ottenendo due alberi di derivazione di tipo left-most per lo stesso input.
<br><br>
Per risolvere, trasformiamo la grammatica nel modo seguente:<br>

          S -> Program EOF

          Program -> Program ; Stmt
                     |  Stmt
          Stmt -> IF Expr THEN Stmt END IF
                     | IF Expr THEN Stmt ELSE Stmt END IF
                     | ID ASSIGN Expr
                     | WHILE Epr LOOP Stmt END LOOP

          Expr ->  Expr Relop Expr' | Expr'

          Expr' ->   ID
                     |  NUMBER

          Relop -> LESS
                    | LESSOREQUAL
                    | GREATER
                    | GREATEROREQUAL
                    | EQUAL
                    | NOTEQUAL
Con lo stesso input 'a < b < c', partendo dal non terminale Expr, deriveremo in questo modo:<br>
Expr -> Expr Relop Expr' -> Expr Relop Expr' Relop Expr' -> Expr' Relop Expr' Relop Expr'
-> ID Relop ID Relop ID
L'ambiguità è quindi risolta.
### Ricorsività sinistra
La grammatica intermedia presenta due casi di ricorsività sinistra, con i non terminali Program e Expr.<br>
Essi possono causare loop infiniti, quindi vanno riscritti in un altro modo.<br>
La produzione 'Program -> Program ; Stmt | Stmt' rappresenta una sequenza di Stmt con un separatore ';', con almeno un non terminale Stmt,
quindi può essere riscritta considerando Stmt come punto di partenza e '; Stmt' come parte ricorsiva (destra)
(Stesso discorso viene applicato per il non terminale Expr):

          S -> Program EOF

          Program -> Stmt Program'
          
          Program' -> ; Stmt Program' | eps
                     
          Stmt -> IF Expr THEN Stmt END IF
                     | IF Expr THEN Stmt ELSE Stmt END IF
                     | ID ASSIGN Expr
                     | WHILE Expr LOOP Stmt END LOOP
          
          Expr -> Expr' Expr''
          
          Expr'' -> Relop Expr' Expr'' | eps

          Expr' ->   ID
                     |  NUMBER

          Relop -> LESS
                    | LESSOREQUAL
                    | GREATER
                    | GREATEROREQUAL
                    | EQUAL
                    | NOTEQUAL
La ricorsione sinistra è quindi rimossa da ogni produzione.

### Fattorizzazione sinistra
Il non terminale Stmt presenta più produzioni che possono essere fattorizzate nel seguente modo:

        Stmt -> If Expr THEN Stmt ElseStmt END IF
                | ID ASSIGN Expr
                | WHILE Expr LOOP Stmt END LOOP

        ElseStmt -> ELSE Stmt | eps

Questa transformazione non introduce ambiguità (i terminali END IF evitano il problema del *dangling-else*) o ricorsioni a sinistra.

## Conclusioni
La grammatica così ottenuta è una grammatica valida per un parser top-down a discesa ricorsiva.

          S -> Program EOF

          Program -> Stmt Program'
          
          Program' -> ; Stmt Program' | eps
                     
          Stmt -> If Expr THEN Stmt ElseStmt END IF
                | ID ASSIGN Expr
                | WHILE Expr LOOP Stmt END LOOP

          ElseStmt -> ELSE Stmt | eps
          
          Expr -> Expr' Expr''
          
          Expr'' -> Relop Expr' Expr'' | eps

          Expr' ->   ID
                     |  NUMBER

          Relop -> LESS
                    | LESSOREQUAL
                    | GREATER
                    | GREATEROREQUAL
                    | EQUAL
                    | NOTEQUAL

# Modifiche al Lexer:
- Aggiunto line-tracking per facilitare il debug di programmi sintatticamente o lessicalmente invalidi;
- Aggiunto supporto per i commenti stile python (singoli per linea, iniziano con #);