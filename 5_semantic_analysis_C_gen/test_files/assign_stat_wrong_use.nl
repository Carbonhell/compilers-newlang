|*
    Esempio di uso improprio dello statement AssignStat.
    Il parsing di questo snippet dovrebbe fallire con l'eccezione "The amount of identifiers should be equal to the amount of values supplied."
|
start:
def main(): void {
    integer a,b,c;
    a,b,c << 1,2;
}