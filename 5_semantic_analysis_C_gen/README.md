# Dettagli relativi ai test per la CI
1) Il test "invalid_wrongoutuse" fallisce per un motivo diverso. Invece di fallire per via dell'utilizzo di un costrutto diverso da Identifier, questo fallisce per via dell'operazione invalida di somma tra una stringa e un intero (taglia+1). Per ovviare a ciò, è presente il test "invalid_wronguseofout.nl" nella cartella "test_files" che mostra un esempio di errore dovuto al passaggio di una espressione invece di un ID come argomento associato ad un parametro out.

# Type system
Il documento contenente le regole di tipo è il seguente: [Type_system_del_linguaggio_NewLang.pdf](Type_system_del_linguaggio_NewLang.pdf)[]()

# Compilazione
Sono presenti varie runConfiguration di IntelliJ per la compilazione dei vari test.
In particolare, è presente la Compound configuration "Newlang to binary" che permette di ottenere un eseguibile a partire
da un file sorgente NewLang. L'eseguibile sarà disponibile dopo l'esecuzione nella cartella c_out.