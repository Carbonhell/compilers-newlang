package esercitazione5.Visitor;

import esercitazione5.SyntaxTreeNodes.*;
import esercitazione5.SyntaxTreeNodes.Expressions.*;
import esercitazione5.Utils.Cast;
import esercitazione5.Utils.Scope;
import esercitazione5.Utils.ScopeEntry;
import esercitazione5.Utils.SemanticException;

import java.util.*;

public class CGenVisitor implements Visitor<String> {
    private int tempVarCounter = 0;

    // Valid only when handling a list of VarDecls
    // This is needed to properly order C variable declarations since NewLang supports the use of vars defined later on
    // in a scope (valid5 test for example)
    private Map<String, Integer> declarations;

    /**
     * Returns the name of a temp variable usable for compiler casts and other operations.
     */
    private String getTempVar() {
        String result = "__NEWLANG__TMP_" + tempVarCounter;
        tempVarCounter++;
        return result;
    }

    private static class IndentedStringBuilder {
        private final StringBuilder _builder;

        public IndentedStringBuilder() {
            _builder = new StringBuilder();
        }

        public void append(String s) {
            _builder.append(s);
        }

        @Override
        public String toString() {
            return _builder.toString().indent(4);
        }
    }
    private String genFunSignature(FunDecl funDecl) throws SemanticException {
        return genFunSignature(funDecl, null);
    }
    /**
     * overrideId allows overriding the fun id
     */
    private String genFunSignature(FunDecl funDecl, String overrideId) throws SemanticException {
        StringBuilder output = new StringBuilder();
        output.append(funDecl.returnType.accept(this));
        output.append(" ");
        output.append(overrideId != null ? overrideId : getIdCode(funDecl.id, false));
        output.append("(");

        ArrayList<String> paramsCode = new ArrayList<>();
        for (ParDecl parDecl : funDecl.parDeclList) {
            paramsCode.add(parDecl.accept(this));
        }
        output.append(String.join(", ", paramsCode));
        output.append(")");
        return output.toString();
    }

    @Override
    public String visitProgram(Program p) throws SemanticException {
        StringBuilder output = new StringBuilder();
        output.append("#include <stdio.h>\n");
        output.append("#include <math.h>\n");
        output.append("#include <stdbool.h>\n");
        output.append("#include <string.h>\n");
        output.append("#include <stdlib.h>\n");
        output.append("\n");
        output.append("char* concat(char *s1, char *s2)\n" +
                "{\n" +
                "    char *result = malloc(strlen(s1) + strlen(s2) + 1); // +1 for the null-terminator\n" +
                "    strcpy(result, s1);\n" +
                "    strcat(result, s2);\n" +
                "    return result;\n" +
                "}\n");
        output.append("char* toString" + Type.INT + "(int x)\n" +
                "{\n" +
                "    int length = snprintf( NULL, 0, \"%d\", x );\n" +
                "    char* str = malloc( length + 1 );\n" +
                "    snprintf( str, length + 1, \"%d\", x );\n" +
                "    return str;\n" +
                "}\n");
        output.append("char* toString" + Type.FLOAT + "(float x)\n" +
                "{\n" +
                "    int length = snprintf( NULL, 0, \"%f\", x );\n" +
                "    char* str = malloc( length + 1 );\n" +
                "    snprintf( str, length + 1, \"%f\", x );\n" +
                "    return str;\n" +
                "}\n");
        output.append("char* toString" + Type.BOOL + "(bool x)\n" +
                "{\n" +
                "    int length = snprintf( NULL, 0, \"%f\", x );\n" +
                "    char* str = malloc( length + 1 );\n" +
                "    snprintf( str, length + 1, \"%f\", x );\n" +
                "    return str;\n" +
                "}\n");
        output.append("char* toString" + Type.CHAR + "(char x)\n" +
                "{\n" +
                "    return (char[2]) { (char) x, '\\0' };\n" +
                "}\n");
        output.append("\n");

        this.declarations = new LinkedHashMap<>();
        for (VarDecl varDecl : p.varDeclList) {
            output.append(varDecl.accept(this));
        }
        this.declarations
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    output.append(entry.getKey());
                });

        this.declarations = null;

        output.append("\n");

        // Do a first pass to declare all the functions so that the order of definition doesn't matter
        for (FunDecl funDecl : p.funDeclList) {
            output.append(this.genFunSignature(funDecl));
            output.append(";\n");
        }
        output.append(p.mainFunDecl.accept(this));

        if (!p.mainFunDecl.id.attribute.equals("main")) {
            output.append("\n");
            output.append(genFunSignature(p.mainFunDecl, "main"));
            output.append(" {");
            output.append(p.mainFunDecl.id.attribute);
            output.append("(");
            ArrayList<String> mainArgs = new ArrayList<>();
            for (ParDecl parDecl : p.mainFunDecl.parDeclList) {
                for (Identifier id : parDecl.idList) {
                    mainArgs.add(id.attribute);
                }
            }
            output.append(String.join(", ", mainArgs));
            output.append(");}");
        }

        output.append("\n");

        for (FunDecl funDecl : p.funDeclList) {
            output.append(funDecl.accept(this));
            output.append("\n");
        }

        return output.toString();
    }

    @Override
    public String visitTrueConstExpr(TrueConstExpr trueConstExpr) {
        return "true";
    }

    @Override
    public String visitIntegerConstExpr(IntegerConstExpr integerConstExpr) {
        return String.valueOf(integerConstExpr.attribute);
    }

    @Override
    public String visitRealConstExpr(RealConstExpr realConstExpr) {
        return String.valueOf(realConstExpr.attribute);
    }

    @Override
    public String visitStringConstExpr(StringConstExpr stringConstExpr) {
        return "\"" + stringConstExpr.attribute + "\"";
    }

    @Override
    public String visitCharConstExpr(CharConstExpr charConstExpr) {
        return "'" + charConstExpr.attribute + "'";
    }

    private String typeToCType(String type) {
        switch (type) {
            case Type.INT -> {
                return "int";
            }
            case Type.FLOAT -> {
                return "double";
            }
            case Type.CHAR -> {
                return "char";
            }
            case Type.STRING -> {
                return "char*";
            }
            case Type.BOOL -> {
                return "bool";
            }
            case Type.VOID -> {
                return "void";
            }
        }
        return null;
    }

    @Override
    public String visitType(Type type) {
        return typeToCType(type.type);
    }

    @Override
    public String visitVarDecl(VarDecl varDecl) throws SemanticException {
        StringBuilder output = new StringBuilder();
        ArrayList<String> stringsInit = new ArrayList<>();

        for (Map.Entry<Identifier, Expr> entry : varDecl.idsInit.entrySet()) {
            // Even though C allows multiple declarations in one line, we split them to properly order them
            // This is needed for forward references
            StringBuilder declaration = new StringBuilder();
            Identifier id = entry.getKey();
            declaration.append(typeToCType(id.getType()));
            declaration.append(" ");
            declaration.append(getIdCode(id, false));
            Expr expr = entry.getValue();
            if(id.getType().equals(Type.STRING)) {
                // TODO: find a better way to specify string size...
                declaration.append(" = (char*) malloc(100)");
            }

            if (expr != null) {
                if(id.getType().equals(Type.STRING)) {
                    stringsInit.add("strcpy(" + id.attribute + ", " + expr.accept(this) + ");\n");
                } else {
                    declaration.append(" = ");
                    declaration.append(expr.accept(this));
                }
            }
            declaration.append(";\n");

            String decl = declaration.toString();
            ScopeEntry scopeEntry = id.getScope().strictLookup(id);
            this.declarations.put(decl, scopeEntry.order);
        }

        for (String s: stringsInit) {
            this.declarations.put(s, Integer.MAX_VALUE); // After all the declarations
            output.append(s);
        }

        // The return value is actually not used since the strings are taken from this.declarations to order them
        return output.toString();
    }

    @Override
    public String visitFunDecl(FunDecl funDecl) throws SemanticException {
        return this.genFunSignature(funDecl) +
                funDecl.body.accept(this) + "\n\n";
    }

    @Override
    public String visitBody(Body body) throws SemanticException {
        IndentedStringBuilder output = new IndentedStringBuilder();

        this.declarations = new LinkedHashMap<>();
        for (VarDecl varDecl : body.varDecls) {
            varDecl.accept(this);
        }
        this.declarations
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    output.append(entry.getKey());
                });

        this.declarations = null;

        output.append("\n");
        for (Stat stat : body.stats) {
            output.append(stat.accept(this));
        }

        return " {\n" + output + "}"; // { inserted outside the builder to avoid weird indentation
    }

    @Override
    public String visitIfStat(IfStat ifStat) throws SemanticException {
        StringBuilder output = new StringBuilder("if (");
        output.append(ifStat.condition.accept(this));
        output.append(")");
        output.append(ifStat.body.accept(this));
        if (ifStat.elseNode != null) {
            output.append(" else");
            output.append(ifStat.elseNode.accept(this));
        }
        output.append("\n");

        return output.toString();
    }

    @Override
    public String visitWhileStat(WhileStat whileStat) throws SemanticException {
        StringBuilder output = new StringBuilder("while (");
        output.append(whileStat.condition.accept(this));
        output.append(")");
        output.append(whileStat.body.accept(this));
        output.append("\n");
        return output.toString();
    }

    @Override
    public String visitForStat(ForStat forStat) throws SemanticException {
        StringBuilder output = new StringBuilder("for (int ");
        output.append(forStat.tempVar.accept(this));
        output.append(" = ");
        output.append(forStat.from);
        output.append("; ");
        output.append(forStat.tempVar.accept(this));
        output.append(forStat.from > forStat.to ? " >= " : " <= ");
        output.append(forStat.to);
        output.append("; ");
        output.append(forStat.tempVar.accept(this));
        output.append(forStat.from > forStat.to ? "--" : "++");
        output.append(")");
        output.append(forStat.body.accept(this));
        output.append("\n");
        return output.toString();
    }

    @Override
    public String visitReadStat(ReadStat readStat) throws SemanticException {
        StringBuilder output = new StringBuilder();

        if(readStat.message != null) {
            output.append("printf(");
            output.append(readStat.message.accept(this));
            output.append(");\n");
        }

        StringBuilder scanf = new StringBuilder("scanf(\"");
        ArrayList<String> scanfPlaceholders = new ArrayList<>();
        ArrayList<String> scanfRefs = new ArrayList<>();
        HashMap<String, String> tempToBool = new HashMap<>();

        // Build the scanf statement. It may contain compiler temp vars for bool types.
        for(Identifier id: readStat.idList) {
            String idType = id.getType();
            // Bools require special handling since there's no scanf impl for them
            if (idType.equals(Type.BOOL)) {
                String tempVar = getTempVar();
                scanfPlaceholders.add(" %c"); // Char that can be "y" or "n"
                scanfRefs.add("&" + tempVar);
                tempToBool.put(tempVar, getIdCode(id, true));
            } else {
                switch (idType) {
                    case Type.STRING -> scanfPlaceholders.add("%s");
                    case Type.INT -> scanfPlaceholders.add("%d");
                    case Type.FLOAT -> scanfPlaceholders.add("%lf");
                    case Type.CHAR -> scanfPlaceholders.add(" %c"); // Whitespace required to properly skip buffer leftovers
                }
                String ref = getIdCode(id, true);
                if(!id.getType().equals(Type.STRING)) {
                    ref = "&"+ref;
                }
                scanfRefs.add(ref);
            }
        }
        scanf.append(String.join(" ", scanfPlaceholders));
        scanf.append("\", ");
        scanf.append(String.join(", ", scanfRefs));
        scanf.append(");\n");

        // Use a temp char var to store the scanf result for bool types (y/N)
        if (tempToBool.size() > 0) {
            output.append("char ");
            output.append(String.join(", ", tempToBool.keySet()));
            output.append(";\n");
        }

        output.append(scanf.toString());

        // Set the original bool vars
        for(Map.Entry<String, String> entry: tempToBool.entrySet()) {
            output.append(entry.getValue());
            output.append(" = ");
            output.append(entry.getKey());
            output.append(" == 'y';\n");
        }

        return output.toString();
    }

    @Override
    public String visitWriteStat(WriteStat writeStat) throws SemanticException {
        StringBuilder output = new StringBuilder();

        output.append("printf(\"");

        ArrayList<String> varsToPrint = new ArrayList<>();
        for(Expr expr: writeStat.exprList) {
            switch (expr.getType()) {
                case Type.STRING -> output.append("%s");
                case Type.INT -> output.append("%d");
                case Type.FLOAT -> output.append("%lf");
                case Type.CHAR -> output.append("%c");
                case Type.BOOL -> output.append("%d");
            }

            varsToPrint.add(expr.accept(this));
        }

        if(writeStat.isWriteln) {
            output.append("\\n");
        }
        output.append("\", ");
        output.append(String.join(", ", varsToPrint));
        output.append(");\n");

        return output.toString();
    }

    @Override
    public String visitAssignStat(AssignStat assignStat) throws SemanticException {
        StringBuilder output = new StringBuilder();
        Iterator<Identifier> idIterator = assignStat.idList.iterator();
        Iterator<Expr> exprIterator = assignStat.exprList.iterator();

        while (idIterator.hasNext() && exprIterator.hasNext()) {
            Identifier id = idIterator.next();
            Expr expr = exprIterator.next();

            if(id.getType().equals(Type.STRING)) {
                output.append("strcpy(");
                output.append(getIdCode(id, true));
                output.append(", ");
                output.append(expr.accept(this));
                output.append(");\n");
            } else {
                output.append(getIdCode(id, true));
                output.append(" = ");
                output.append(expr.accept(this));
                output.append(";\n");
            }
        }

        return output.toString();
    }

    @Override
    public String visitFunCall(FunCall funCall) throws SemanticException {
        ScopeEntry funType = funCall.getScope().lookup(funCall.id);
        Map<String, Object> extraData = funType.extraData;
        Iterator<Object> parDeclsIter = extraData.values().iterator();

        StringBuilder output = new StringBuilder();
        output.append(funCall.id.accept(this));
        output.append("(");

        ArrayList<String> args = new ArrayList<>();
        for (Expr expr : funCall.exprList) {
            String exprCode = expr.accept(this);
            SemanticVisitor.ParDeclInfo parDeclInfo = (SemanticVisitor.ParDeclInfo) parDeclsIter.next();
            if(parDeclInfo.isOut() && !parDeclInfo.type().equals(Type.STRING)) {
                exprCode = "&" + exprCode;
            }
            args.add(exprCode);
        }
        output.append(String.join(", ", args));

        output.append(")");
        if (funCall.isStat) {
            output.append(";\n");
        }
        return output.toString();
    }

    /**
     * Util to get the code for an identifier, based on whether we're declaring or using it.
     * Needed in particular for uses of out params, which need the dereference operator prefixed.
     */
    public String getIdCode(Identifier id, boolean isUse) throws SemanticException {
        if(isUse) {
            return id.accept(this);
        }
        return id.attribute;
    }

    /**
     * Only identifier uses should call accept on this node.
     */
    @Override
    public String visitIdentifier(Identifier identifier) throws SemanticException {
        String idCode = identifier.attribute;
        Scope scope = identifier.getScope();
        if(scope != null) {
            ScopeEntry entry = scope.lookup(identifier);
            if(entry.kind == ScopeEntry.SymbolType.PARAMETER) {
                // Strings are already pointers
                if((Boolean) entry.extraData.get("isOut") && !entry.type.equals(Type.STRING)) {
                    idCode = "*" + idCode;
                }
            }
        }
        return idCode;
    }

    @Override
    public String visitAddExpr(AddExpr addExpr) throws SemanticException {
        return addExpr.first.accept(this) + " + " + addExpr.second.accept(this);
    }

    @Override
    public String visitSubExpr(SubExpr subExpr) throws SemanticException {
        return subExpr.first.accept(this) + " - " + subExpr.second.accept(this);
    }

    @Override
    public String visitTimesExpr(TimesExpr timesExpr) throws SemanticException {
        return timesExpr.first.accept(this) + " * " + timesExpr.second.accept(this);
    }

    @Override
    public String visitDivExpr(DivExpr divExpr) throws SemanticException {
        return divExpr.first.accept(this) + " / " + divExpr.second.accept(this);
    }

    @Override
    public String visitAndExpr(AndExpr andExpr) throws SemanticException {
        return andExpr.first.accept(this) + " && " + andExpr.second.accept(this);
    }

    @Override
    public String visitPowExpr(PowExpr powExpr) throws SemanticException {
        return "pow((double)" +
                powExpr.first.accept(this) +
                ", (double)" +
                powExpr.second.accept(this) +
                ")";
    }

    @Override
    public String visitStrConcatExpr(StrConcatExpr strConcatExpr) throws SemanticException {
        StringBuilder output = new StringBuilder("concat(");
        String firstCode = strConcatExpr.first.accept(this);
        if(!strConcatExpr.first.getType().equals(Type.STRING)) {
            output.append("toString");
            output.append(strConcatExpr.first.getType());
            output.append("(");
            output.append(firstCode);
            output.append(")");
        } else {
            output.append(firstCode);
        }
        output.append(", ");

        String secondCode = strConcatExpr.second.accept(this);
        if(!strConcatExpr.second.getType().equals(Type.STRING)) {
            output.append("toString");
            output.append(strConcatExpr.second.getType());
            output.append("(");
            output.append(secondCode);
            output.append(")");
        } else {
            output.append(secondCode);
        }
        output.append(")");
        return output.toString();
        //return "concat(" + strConcatExpr.first.accept(this) + ", " + strConcatExpr.second.accept(this) + ")";
        //return "strcat(" + strConcatExpr.first.accept(this) + ", " + strConcatExpr.second.accept(this) + ")";
        //return strConcatExpr.first.accept(this) + " + " + strConcatExpr.second.accept(this);
    }

    @Override
    public String visitOrExpr(OrExpr orExpr) throws SemanticException {
        return orExpr.first.accept(this) + " || " + orExpr.second.accept(this);
    }

    @Override
    public String visitGTExpr(GTExpr gtExpr) throws SemanticException {
        return gtExpr.first.accept(this) + " > " + gtExpr.second.accept(this);
    }

    @Override
    public String visitGEExpr(GEExpr geExpr) throws SemanticException {
        return geExpr.first.accept(this) + " >= " + geExpr.second.accept(this);
    }

    @Override
    public String visitLTExpr(LTExpr ltExpr) throws SemanticException {
        return ltExpr.first.accept(this) + " < " + ltExpr.second.accept(this);
    }

    @Override
    public String visitLEExpr(LEExpr leExpr) throws SemanticException {
        return leExpr.first.accept(this) + " <= " + leExpr.second.accept(this);
    }

    @Override
    public String visitEQExpr(EQExpr eqExpr) throws SemanticException {
        if(eqExpr.first.getType().equals(Type.STRING)) {
            return "strcmp(" + eqExpr.first.accept(this) + ", " + eqExpr.second.accept(this) + ") == 0";
        }
        return eqExpr.first.accept(this) + " == " + eqExpr.second.accept(this);
    }

    @Override
    public String visitNEExpr(NEExpr neExpr) throws SemanticException {
        if(neExpr.first.getType().equals(Type.STRING)) {
            return "strcmp(" + neExpr.first.accept(this) + ", " + neExpr.second.accept(this) + ") != 0";
        }
        return neExpr.first.accept(this) + " != " + neExpr.second.accept(this);
    }

    @Override
    public String visitMinusExpr(MinusExpr minusExpr) throws SemanticException {
        return "-" + minusExpr.expr.accept(this);
    }

    @Override
    public String visitNotExpr(NotExpr notExpr) throws SemanticException {
        return "!" + notExpr.expr.accept(this);
    }

    @Override
    public String visitFalseConstExpr(FalseConstExpr falseConstExpr) {
        return "false";
    }

    @Override
    public String visitParDecl(ParDecl parDecl) throws SemanticException {
        StringBuilder output = new StringBuilder();
        ArrayList<String> params = new ArrayList<>();

        for (Identifier id : parDecl.idList) {
            StringBuilder param = new StringBuilder();
            param.append(parDecl.type.accept(this));
            if (parDecl.isOut && !parDecl.type.type.equals(Type.STRING)) {
                param.append("*");
            }
            param.append(" ");
            param.append(getIdCode(id, false));
            params.add(param.toString());
        }

        output.append(String.join(", ", params));
        return output.toString();
    }

    @Override
    public String visitReturnStat(ReturnStat returnStat) throws SemanticException {
        StringBuilder stringBuilder = new StringBuilder("return");
        if (returnStat.returnVal != null) {
            stringBuilder.append(" ");
            stringBuilder.append(returnStat.returnVal.accept(this));
        }
        stringBuilder.append(";");
        return stringBuilder.toString();
    }

    @Override
    public String visitCast(Cast cast) throws SemanticException {
        StringBuilder output = new StringBuilder();
        switch(cast.getType()) {
            case Type.STRING -> {
                output.append("toString");
                output.append(cast.expr.getType());
                output.append("(");
                output.append(cast.expr.accept(this));
                output.append(")");
            }
            case Type.FLOAT -> {
                output.append("(double)");
                output.append(cast.expr.accept(this));
            }
        }
        return output.toString();
    }
}
