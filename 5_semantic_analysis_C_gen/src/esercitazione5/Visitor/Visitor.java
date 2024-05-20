package esercitazione5.Visitor;

import esercitazione5.SyntaxTreeNodes.*;
import esercitazione5.SyntaxTreeNodes.Expressions.*;
import esercitazione5.Utils.Cast;
import esercitazione5.Utils.SemanticException;

public interface Visitor<T> {
    T visitProgram(Program p) throws SemanticException;

    T visitTrueConstExpr(TrueConstExpr trueConstExpr);

    T visitIntegerConstExpr(IntegerConstExpr integerConstExpr);

    T visitRealConstExpr(RealConstExpr realConstExpr);

    T visitStringConstExpr(StringConstExpr stringConstExpr);

    T visitCharConstExpr(CharConstExpr charConstExpr);

    T visitType(Type type);

    T visitVarDecl(VarDecl varDecl) throws SemanticException;


    T visitFunDecl(FunDecl funDecl) throws SemanticException;

    T visitBody(Body body) throws SemanticException;

    T visitIfStat(IfStat ifStat) throws SemanticException;

    T visitWhileStat(WhileStat whileStat) throws SemanticException;

    T visitForStat(ForStat forStat) throws SemanticException;

    T visitReadStat(ReadStat readStat) throws SemanticException;

    T visitWriteStat(WriteStat writeStat) throws SemanticException;

    T visitAssignStat(AssignStat assignStat) throws SemanticException;

    T visitFunCall(FunCall funCall) throws SemanticException;

    T visitIdentifier(Identifier identifier) throws SemanticException;

    T visitAddExpr(AddExpr addExpr) throws SemanticException;

    T visitSubExpr(SubExpr subExpr) throws SemanticException;

    T visitTimesExpr(TimesExpr timesExpr) throws SemanticException;

    T visitDivExpr(DivExpr divExpr) throws SemanticException;

    T visitAndExpr(AndExpr andExpr) throws SemanticException;

    T visitPowExpr(PowExpr powExpr) throws SemanticException;

    T visitStrConcatExpr(StrConcatExpr strConcatExpr) throws SemanticException;

    T visitOrExpr(OrExpr orExpr) throws SemanticException;

    T visitGTExpr(GTExpr gtExpr) throws SemanticException;

    T visitGEExpr(GEExpr geExpr) throws SemanticException;

    T visitLTExpr(LTExpr ltExpr) throws SemanticException;

    T visitLEExpr(LEExpr leExpr) throws SemanticException;

    T visitEQExpr(EQExpr eqExpr) throws SemanticException;

    T visitNEExpr(NEExpr neExpr) throws SemanticException;

    T visitMinusExpr(MinusExpr minusExpr) throws SemanticException;

    T visitNotExpr(NotExpr notExpr) throws SemanticException;

    T visitFalseConstExpr(FalseConstExpr falseConstExpr);

    T visitParDecl(ParDecl parDecl) throws SemanticException;

    T visitReturnStat(ReturnStat returnStat) throws SemanticException;

    T visitCast(Cast cast) throws SemanticException;
}
