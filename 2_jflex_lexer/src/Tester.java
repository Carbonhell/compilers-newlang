import java_cup.runtime.Symbol;

public class Tester {
    // Refactored from jflex standalone implementation of a tester
    public static void main(String[] argv) {
        if (argv.length == 0) {
            System.out.println("Usage : java Lexer <inputfile(s)>");
        }
        else {
            int firstFilePos = 0;
            String encodingName = "UTF-8";
            for (int i = firstFilePos; i < argv.length; i++) {
                Lexer scanner;
                try {
                    java.io.FileInputStream stream = new java.io.FileInputStream(argv[i]);
                    java.io.Reader reader = new java.io.InputStreamReader(stream, encodingName);
                    scanner = new Lexer(reader);
                    Symbol s;
                    while ( (s = scanner.next_token()).sym != Token.EOF ) System.out.println(Token.symbolToString(s));
                }
                catch (java.io.FileNotFoundException e) {
                    System.out.println("File not found : \""+argv[i]+"\"");
                }
                catch (java.io.IOException e) {
                    System.out.println("IO error scanning file \""+argv[i]+"\"");
                    System.out.println(e.getMessage());
                }
                catch (Exception e) {
                    System.out.println("Unexpected exception:");
                    e.printStackTrace();
                }
                finally {
                    System.out.println("\nString table contents:\n");
                    for (String lexeme : Lexer.stringTable.keySet()) {
                        Symbol sym = Lexer.stringTable.get(lexeme);
                        System.out.println("Lexeme: " + lexeme + ", token: " + Token.symbolToString(sym));
                    }
                }
            }
        }
    }
}
