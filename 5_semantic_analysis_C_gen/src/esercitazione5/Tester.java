package esercitazione5;

import esercitazione5.Lex;
import esercitazione5.SyntaxTreeNodes.Program;
import esercitazione5.Visitor.CGenVisitor;
import esercitazione5.Visitor.SemanticVisitor;
import esercitazione5.parser;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Tester {
    public static void main(String[] argv) throws Exception {
        if (argv.length == 0) {
            System.out.println("Usage : java Lexer <inputfile(s)>");
            return;
        }
        java.io.FileInputStream stream = new java.io.FileInputStream(argv[0]);
        java.io.Reader reader = new java.io.InputStreamReader(stream, StandardCharsets.UTF_8);
        parser p = new parser(new Lex(reader));

        Program program = (Program) p.parse().value;
        //XMLVisitor xmlParser = new XMLVisitor();
        //System.out.println(xmlParser.visitProgram(program));
        SemanticVisitor semanticVisitor = new SemanticVisitor();
        semanticVisitor.visitProgram(program);

        CGenVisitor cgen = new CGenVisitor();
        String code = cgen.visitProgram(program);
        String filename = (new File(argv[0]).getName());
        filename = filename.substring(0, filename.lastIndexOf('.'));
        if(argv.length == 2) {
            filename = "out";
        }
        try (PrintWriter out = new PrintWriter("c_out" + File.separator + filename + ".c")) {
            out.print(code);
        }
    }

}
