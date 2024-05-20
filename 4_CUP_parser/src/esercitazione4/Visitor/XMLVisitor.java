package esercitazione4.Visitor;

import esercitazione4.SyntaxTreeNodes.*;
import esercitazione4.SyntaxTreeNodes.Expressions.*;

import java.util.Optional;

public class XMLVisitor implements Visitor<String> {
    @Override
    public String visitProgram(Program p) {
        StringBuilder output = new StringBuilder("<Program>");
        // TODO wrapper?
        Optional<String> varDecls = p.varDeclList
                .stream()
                .map(varDecl -> varDecl.accept(this))
                .reduce((a, b) -> a + b);
        varDecls.ifPresent(output::append);

        Optional<String> funDecls = p.funDeclList
                .stream()
                .map(varDecl -> varDecl.accept(this))
                .reduce((a, b) -> a + b);
        funDecls.ifPresent(output::append);

        output.append(p.mainFunDecl.accept(this));

        output.append("</Program>");
        return output.toString();
    }

    @Override
    public String visitTrueConstExpr(TrueConstExpr trueConstExpr) {
        return "<BoolConstExpr>true</BoolConstExpr>";
    }

    @Override
    public String visitIntegerConstExpr(IntegerConstExpr integerConstExpr) {
        return String.format("<IntegerConstExpr>%d</IntegerConstExpr>", integerConstExpr.attribute);
    }

    @Override
    public String visitRealConstExpr(RealConstExpr realConstExpr) {
        return String.format("<RealConstExpr>%f</RealConstExpr>", realConstExpr.attribute);
    }

    @Override
    public String visitStringConstExpr(StringConstExpr stringConstExpr) {
        return String.format("<StringConstExpr>%s</StringConstExpr>", stringConstExpr.attribute);
    }

    @Override
    public String visitCharConstExpr(CharConstExpr charConstExpr) {
        return String.format("<CharConstExpr>%c</CharConstExpr>", charConstExpr.attribute);
    }

    @Override
    public String visitType(Type type) {
        return String.format("<Type>%s</Type>", type.type);
    }

    @Override // TODO use LinkedHashMaps everywhere
    public String visitVarDecl(VarDecl varDecl) {
        StringBuilder output = new StringBuilder("<VarDecl>");

        if (varDecl.type != null) {
            output.append(varDecl.type.accept(this));
        }

        // TODO check if other maps have nullable values
        Optional<String> inits = varDecl.idsInit
                .entrySet()
                .stream()
                .map(e -> {
                    StringBuilder decl = new StringBuilder("<Decl>");

                    decl.append(e.getKey().accept(this));
                    if(e.getValue() != null) {
                        decl.append(e.getValue().accept(this));
                    }

                    decl.append("</Decl>");
                    return decl.toString();
                })
                .reduce((a, b) -> a + b);
        inits.ifPresent(output::append);

        output.append("</VarDecl>");
        return output.toString();
    }

    @Override
    public String visitFunDecl(FunDecl funDecl) {
        StringBuilder output = new StringBuilder("<FunDecl>");
        output.append(funDecl.id.accept(this));

        Optional<String> parDecls = funDecl.parDeclList
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        parDecls.ifPresent(output::append);

        output.append(funDecl.returnType.accept(this));
        output.append(funDecl.body.accept(this));

        output.append("</FunDecl>");
        return output.toString();
    }

    @Override
    public String visitBody(Body body) {
        StringBuilder output = new StringBuilder("<Body>");

        Optional<String> varDecls = body.varDecls
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        varDecls.ifPresent(output::append);

        Optional<String> stats = body.stats
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        stats.ifPresent(output::append);

        output.append("</Body>");
        return output.toString();
    }

    @Override
    public String visitIfStat(IfStat ifStat) {
        StringBuilder output = new StringBuilder("<IfStat>");

        output.append(ifStat.condition.accept(this));
        output.append(ifStat.body.accept(this));
        if (ifStat.elseNode != null) {
            output.append(ifStat.elseNode.accept(this));
        }

        output.append("</IfStat>");
        return output.toString();
    }

    @Override
    public String visitWhileStat(WhileStat whileStat) {

        return "<WhileStat>%s%s</WhileStat>".formatted(whileStat.condition.accept(this), whileStat.body.accept(this));
    }

    @Override
    public String visitForStat(ForStat forStat) {
        return "<ForStat from=\"%d\" to=\"%d\">%s%s</ForStat>".formatted(forStat.from, forStat.to, forStat.tempVar.accept(this), forStat.body.accept(this));
    }

    @Override
    public String visitReadStat(ReadStat readStat) {
        StringBuilder output = new StringBuilder("<ReadStat>");

        Optional<String> ids = readStat.idList
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        ids.ifPresent(output::append);

        if (readStat.message != null) {
            output.append(readStat.message.accept(this));
        }

        output.append("</ReadStat>");
        return output.toString();
    }

    @Override
    public String visitWriteStat(WriteStat writeStat) {
        StringBuilder output = new StringBuilder("<WriteStat isWriteln=\"%b\">".formatted(writeStat.isWriteln));

        Optional<String> ids = writeStat.exprList
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        ids.ifPresent(output::append);

        output.append("</WriteStat>");
        return output.toString();
    }

    @Override
    public String visitAssignStat(AssignStat assignStat) {
        StringBuilder output = new StringBuilder("<AssignStat>");

        Optional<String> ids = assignStat.idList
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        ids.ifPresent(output::append);

        Optional<String> exprs = assignStat.exprList
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        exprs.ifPresent(output::append);

        output.append("</AssignStat>");
        return output.toString();
    }

    @Override
    public String visitFunCall(FunCall funCall) {
        StringBuilder output = new StringBuilder("<FunCall>");

        output.append(funCall.id.accept(this));

        Optional<String> exprs = funCall.exprList
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        exprs.ifPresent(output::append);

        output.append("</FunCall>");
        return output.toString();
    }

    @Override
    public String visitIdentifier(Identifier identifier) {
        return "<Identifier>%s</Identifier>".formatted(identifier.attribute);
    }

    @Override
    public String visitAddExpr(AddExpr addExpr) {
        return "<AddExpr>%s%s</AddExpr>".formatted(addExpr.first.accept(this), addExpr.second.accept(this));
    }

    @Override
    public String visitSubExpr(SubExpr subExpr) {
        return "<SubExpr>%s%s</SubExpr>".formatted(subExpr.first.accept(this), subExpr.second.accept(this));
    }

    @Override
    public String visitTimesExpr(TimesExpr timesExpr) {
        return "<TimesExpr>%s%s</TimesExpr>".formatted(timesExpr.first.accept(this), timesExpr.second.accept(this));
    }

    @Override
    public String visitDivExpr(DivExpr divExpr) {
        return "<DivExpr>%s%s</DivExpr>".formatted(divExpr.first.accept(this), divExpr.second.accept(this));
    }

    @Override
    public String visitAndExpr(AndExpr andExpr) {
        return "<AndExpr>%s%s</AndExpr>".formatted(andExpr.first.accept(this), andExpr.second.accept(this));
    }

    @Override
    public String visitPowExpr(PowExpr powExpr) {
        return "<PowExpr>%s%s</PowExpr>".formatted(powExpr.first.accept(this), powExpr.second.accept(this));
    }

    @Override
    public String visitStrConcatExpr(StrConcatExpr strConcatExpr) {
        return "<StrConcatExpr>%s%s</StrConcatExpr>".formatted(strConcatExpr.first.accept(this), strConcatExpr.second.accept(this));
    }

    @Override
    public String visitOrExpr(OrExpr orExpr) {
        return "<OrExpr>%s%s</OrExpr>".formatted(orExpr.first.accept(this), orExpr.second.accept(this));
    }

    @Override
    public String visitGTExpr(GTExpr gtExpr) {
        return "<GTExpr>%s%s</GTExpr>".formatted(gtExpr.first.accept(this), gtExpr.second.accept(this));
    }

    @Override
    public String visitGEExpr(GEExpr geExpr) {
        return "<GEExpr>%s%s</GEExpr>".formatted(geExpr.first.accept(this), geExpr.second.accept(this));
    }

    @Override
    public String visitLTExpr(LTExpr ltExpr) {
        return "<LTExpr>%s%s</LTExpr>".formatted(ltExpr.first.accept(this), ltExpr.second.accept(this));
    }

    @Override
    public String visitLEExpr(LEExpr leExpr) {
        return "<LEExpr>%s%s</LEExpr>".formatted(leExpr.first.accept(this), leExpr.second.accept(this));
    }

    @Override
    public String visitEQExpr(EQExpr eqExpr) {
        return "<EQExpr>%s%s</EQExpr>".formatted(eqExpr.first.accept(this), eqExpr.second.accept(this));
    }

    @Override
    public String visitNEExpr(NEExpr neExpr) {
        return "<NEExpr>%s%s</NEExpr>".formatted(neExpr.first.accept(this), neExpr.second.accept(this));
    }

    @Override
    public String visitMinusExpr(MinusExpr minusExpr) {
        return "<MinusExpr>%s</MinusExpr>".formatted(minusExpr.expr.accept(this));
    }

    @Override
    public String visitNotExpr(NotExpr notExpr) {
        return "<NotExpr>%s</NotExpr>".formatted(notExpr.expr.accept(this));
    }

    @Override
    public String visitFalseConstExpr(FalseConstExpr falseConstExpr) {
        return "<BoolConstExpr>false</BoolConstExpr>";
    }

    @Override
    public String visitParDecl(ParDecl parDecl) {
        StringBuilder output = new StringBuilder("<ParDecl isOut=\"%b\">".formatted(parDecl.isOut));

        output.append(parDecl.type.accept(this));

        Optional<String> ids = parDecl.idList
                .stream()
                .map(e -> String.format("%s", e.accept(this)))
                .reduce((a, b) -> a + b);
        ids.ifPresent(output::append);

        output.append("</ParDecl>");
        return output.toString();
    }

    @Override
    public String visitReturnStat(ReturnStat returnStat) {
        StringBuilder output = new StringBuilder("<ReturnStat>");

        if (returnStat.returnVal != null) {
            output.append(returnStat.returnVal.accept(this));
        }

        output.append("</ReturnStat>");
        return output.toString();
    }
}
