|*
 Programma esemplificativo del linguaggio NewLang a volte volutamente ridondante. 

 Questo programma calcola e stampa la somma di due numeri, incrementata di 1.
 Inoltre la definisce 'grande' se è superiore a 100 altrimenti 'piccola'

 FIXES:
 1) Tutti gli utilizzi del tipo "real" sono stati convertiti in "float" come specificato nella definizione del lessico.
 2) Gli utilizzi erronei di apostrofi come delimitatori di stringhe sono stati rimpiazzati con le virgolette
    (es. 'grande' e 'piccola' -> "grande" e "piccola").
 3) La definizione della funzione "esempio" non aveva il tipo di ritorno definito. È stato aggiunto come "void".
 4) La prima dichiarazione di variabili della funzione "esempio", "var a << 1, b << 2.2, x = 3;", utilizzava il token T_EQ
    erroneamente invece del token T_ASSIGN.
 5) Gli utilizzi di statement di tipo WriteStat non comprendevano l'utilizzo di parentesi attorno all'espressione da stampare.
 6) Nella funzione stampa, l'ordine dei token T_WRITELN e delle espressioni da stampare era invertito.
 7) Nel while loop della funzione esempio, era presente un errore lessicale (carattere illegale @)
 8) I body dei loop for/while terminavano con un punto e virgola, non permesso nella definizione sintattica.
|



|| fa somma e restituisce anche la taglia del numero, oltre che il risultato
def sommac(integer a, d | float b | out string size): float
{
	float result;
	
	result  <<  a + b + c + d;

	if result > 100 then{
		var valore << "grande";
 		size << valore; }
	else {	
		var valore << "piccola";
 		size << valore;
	}

	return result;
}


var c << 1;


|| programma principale
start:
def esempio(): void{
	var a << 1, b << 2.2, x << 3;
	string taglia, ans1;
	var ans << "no";
	float risultato << sommac(a, x, b, taglia);

	stampa("la somma di " & a & " e " & b & " incrementata di " & c & " è " & taglia);
	stampa("ed è pari a " & risultato);

	("vuoi continuare? (si/no) - inserisci due volte la risposta")  -->! ;
	ans, ans1 <--;
	while ans = "si" loop {
		a <-- "inserisci un intero:";
	    b <-- "inserisci un reale:";
		risultato << sommac(a, b, taglia);
		stampa("la somma di " & a & " e " & b &  " incrementata di " & c & " è " & taglia);
		stampa(" ed è pari a " & risultato);
		ans <-- "vuoi continuare? (si/no):\t";
	}
	
	("") -->! ;
    ("ciao") -->;

}


|| stampa il messaggio dopo 4 ritorni a capo
def stampa(string messaggio): void {

	for i << 1 to 4 loop {
		("") -->!;
	}

	(messaggio) --> ;

}

|*
****** OUTPUT ATTESO ********




la somma di 1 e 2.2 incrementata di 1 è piccola
ed è pari a 3.2
vuoi continuare? (si/no) - inserisci due volte la risposta 	
si 
si
inserisci un intero: 50
inserisci un reale: 60




la somma di 50 e 60.0 incrementata di 1 è grande
ed è pari a 111.0
vuoi continuare? (si/no) - inserisci due volte la risposta	
no  
no

ciao
|