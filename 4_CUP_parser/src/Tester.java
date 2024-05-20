import esercitazione4.Lex;
import esercitazione4.SyntaxTreeNodes.Program;
import esercitazione4.Visitor.XMLVisitor;
import esercitazione4.parser;

import java.nio.charset.StandardCharsets;

public class Tester {
    public static void main(String[] argv) throws Exception {
        if (argv.length == 0) {
            System.out.println("Usage : java Lexer <inputfile(s)>");
            return;
        }
        System.out.println(argv[0]);
        java.io.FileInputStream stream = new java.io.FileInputStream(argv[0]);
        java.io.Reader reader = new java.io.InputStreamReader(stream, StandardCharsets.UTF_8);
        parser p = new parser(new Lex(reader));

        Program program = (Program) p.debug_parse().value;
        XMLVisitor xmlParser = new XMLVisitor();
        System.out.println(xmlParser.visitProgram(program));

    }

}
