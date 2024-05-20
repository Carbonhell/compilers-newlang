package esercitazione4.Visitor;

import esercitazione4.SyntaxTreeNodes.*;
import esercitazione4.SyntaxTreeNodes.Expressions.*;

public interface Visitor<T> {
    T visitProgram(Program p);

    T visitTrueConstExpr(TrueConstExpr trueConstExpr);

    T visitIntegerConstExpr(IntegerConstExpr integerConstExpr);

    T visitRealConstExpr(RealConstExpr realConstExpr);

    T visitStringConstExpr(StringConstExpr stringConstExpr);

    T visitCharConstExpr(CharConstExpr charConstExpr);

    T visitType(Type type);

    T visitVarDecl(VarDecl varDecl);


    T visitFunDecl(FunDecl funDecl);

    T visitBody(Body body);

    T visitIfStat(IfStat ifStat);

    T visitWhileStat(WhileStat whileStat);

    T visitForStat(ForStat forStat);

    T visitReadStat(ReadStat readStat);

    T visitWriteStat(WriteStat writeStat);

    T visitAssignStat(AssignStat assignStat);

    T visitFunCall(FunCall funCall);

    T visitIdentifier(Identifier identifier);

    T visitAddExpr(AddExpr addExpr);

    T visitSubExpr(SubExpr subExpr);

    T visitTimesExpr(TimesExpr timesExpr);

    T visitDivExpr(DivExpr divExpr);

    T visitAndExpr(AndExpr andExpr);

    T visitPowExpr(PowExpr powExpr);

    T visitStrConcatExpr(StrConcatExpr strConcatExpr);

    T visitOrExpr(OrExpr orExpr);

    T visitGTExpr(GTExpr gtExpr);

    T visitGEExpr(GEExpr geExpr);

    T visitLTExpr(LTExpr ltExpr);

    T visitLEExpr(LEExpr leExpr);

    T visitEQExpr(EQExpr eqExpr);

    T visitNEExpr(NEExpr neExpr);

    T visitMinusExpr(MinusExpr minusExpr);

    T visitNotExpr(NotExpr notExpr);

    T visitFalseConstExpr(FalseConstExpr falseConstExpr);

    T visitParDecl(ParDecl parDecl);

    T visitReturnStat(ReturnStat returnStat);
}
