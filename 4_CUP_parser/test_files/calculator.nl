|*
    Calculator.nl
|

var OP_STOP         << 0,
    OP_ADD          << 1,
    OP_SUB          << 2,
    OP_MUL          << 3,
    OP_DIV          << 4,
    OP_POW          << 5,
    OP_STR_CONCAT   << 6,
    OP_OR           << 7,
    OP_AND          << 8,
    OP_NOT          << 9,
    OP_GT           << 10,
    OP_GE           << 11,
    OP_LT           << 12,
    OP_LE           << 13,
    OP_EQ           << 14,
    OP_NE           << 15;

def printIntro(): void {
    ("Calculator.nl") -->!;
    ("Specifica il tipo di operazione da eseguire") -->!;
    ("\t" & OP_ADD          & ":  Addizione") -->!;
    ("\t" & OP_SUB          & ":  Sottrazione") -->!;
    ("\t" & OP_MUL          & ":  Moltiplicazione") -->!;
    ("\t" & OP_DIV          & ":  Divisione") -->!;
    ("\t" & OP_POW          & ":  Potenza") -->!;
    ("\t" & OP_STR_CONCAT   & ":  Concatenazione di stringhe") -->!;
    ("\t" & OP_OR           & ":  OR logico") -->!;
    ("\t" & OP_AND          & ":  AND logico") -->!;
    ("\t" & OP_NOT          & ":  NOT logico") -->!;
    ("\t" & OP_GT           & ": Maggiore di") -->!;
    ("\t" & OP_GE           & ": Maggiore di o uguale") -->!;
    ("\t" & OP_LT           & ": Minore di") -->!;
    ("\t" & OP_LE           & ": Minore di o uguale") -->!;
    ("\t" & OP_EQ           & ": Uguaglianza") -->!;
    ("\t" & OP_NE           & ": Disuguaglianza") -->!;
    ("") -->!;
    ("\t" & OP_STOP         & ": Chiudi il programma") -->!;
}

start:
def main(): void {
    integer op;
    printIntro();
    op <--;

    while op <> OP_STOP loop {
        if op = OP_ADD then {
            handleSum();
        }
        if op = OP_SUB then {
            handleSub();
        }
        if op = OP_MUL then {
            handleMul();
        }
        if op = OP_DIV then {
            handleDiv();
        }
        if op = OP_POW then {
            handlePow();
        }
        if op = OP_STR_CONCAT then {
            handleStrConcat();
        }
        if op = OP_OR then {
            handleOr();
        }
        if op = OP_AND then {
            handleAnd();
        }
        if op = OP_NOT then {
            handleNot();
        }
        if op = OP_GT then {
            handleGt();
        }
        if op = OP_GE then {
            handleGe();
        }
        if op = OP_LT then {
            handleLt();
        }
        if op = OP_LE then {
            handleLe();
        }
        if op = OP_EQ then {
            handleEq();
        }
        if op = OP_NE then {
            handleNe();
        }

        op <-- "Specifica il prossimo tipo di operazione da eseguire, o inserisci '0' per terminare il programma.";
    }

    ("Arrivederci!") -->!;
}

|*
    Writes a single user input in the out param, checking if the user wants to return to the menu.
    The operand out param is only valid if the return value is equal to true.
|
def getRealOperand(out float operand): boolean {
    string output;
    output <-- "Specifica il valore del prossimo operando o scrivi 'stop' per tornare al menu iniziale: ";
    if output = "stop" then {
        return false;
    }
    operand << output; || Implicit type conversion
    return true;
}

def getStringOperand(out string operand): boolean {
    operand <-- "Specifica il valore del prossimo operando o scrivi 'stop' per tornare al menu iniziale: ";
    if operand = "stop" then {
        return false;
    }
    return true;
}

def getBooleanOperand(out boolean operand): boolean {
    string output;
    output <-- "Specifica il valore del prossimo operando (true o false) o scrivi 'stop' per tornare al menu iniziale: ";
    if output = "stop" then {
        return false;
    }
    operand << output; || Implicit type conversion
    return true;
}

|| Shortcut to call getRealOperand on two operands.
def getRealOperands(out float first | out float second): boolean {
    boolean success << getRealOperand(first);
    if not success then {
        return false;
    }
    success << getRealOperand(second);
    return success;
}

def getStringOperands(out string first | out string second): boolean {
    boolean success << getStringOperand(first);
    if not success then {
        return false;
    }
    success << getStringOperand(second);
    return success;
}

def getBooleanOperands(out boolean first | out boolean second): boolean {
    boolean success << getRealOperand(first);
    if not success then {
        return false;
    }
    success << getRealOperand(second);
    return success;
}

def handleSum(): void {
    float first;
    float second;
    float output;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    output << first + second;
    ("La somma di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handleSub(): void {
    float first;
    float second;
    float output;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    output << first - second;
    ("La sottrazione di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handleMul(): void {
    float first;
    float second;
    float output;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    output << first * second;
    ("La moltiplicazione di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handleDiv(): void {
    float first;
    float second;
    float output;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    output << first / second;
    ("La divisione di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handlePow(): void {
    float first;
    float second;
    float output;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    output << first ^ second;
    ("La potenza di " & a & " alla " & b & " è uguale a: " & output) -->!;
}

def handleStrConcat(): void {
    string first;
    string second;
    string output;
    boolean success << getStringOperands(first, second);
    if not success then {
        return;
    }

    output << first & second;
    ("La concatenazione di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handleOr(): void {
    boolean first;
    boolean second;
    boolean output;
    boolean success << getBooleanOperands(first, second);
    if not success then {
        return;
    }

    output << first or second;
    ("L'or di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handleAnd(): void {
    boolean first;
    boolean second;
    boolean output;
    boolean success << getBooleanOperands(first, second);
    if not success then {
        return;
    }

    output << first or second;
    ("L'and di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handleNot(): void {
    boolean operand;
    boolean output;
    boolean success << getBooleanOperand(operand);
    if not success then {
        return;
    }

    output << not operand;
    ("La not di " & a & " e " & b & " è uguale a: " & output) -->!;
}

def handleGt(): void {
    float first;
    float second;
    boolean result;
    string resultOutput;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    result << first > second;
    resultOutput << "L'operando " & a & " è ";
    if result then {
        resultOutput << resultOutput & " maggiore ";
    } else {
        resultOutput << resultOutput & " minore o uguale";
    }
    resultOutput << resultOutput & " di " & b;

    (resultOutput) -->!;
}

def handleGe(): void {
    float first;
    float second;
    boolean result;
    string resultOutput;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    result << first >= second;
    resultOutput << "L'operando " & a & " è ";
    if result then {
        resultOutput << resultOutput & " maggiore o uguale ";
    } else {
        resultOutput << resultOutput & " minore ";
    }
    resultOutput << resultOutput & " di " & b;

    (resultOutput) -->!;
}

def handleLt(): void {
    float first;
    float second;
    boolean result;
    string resultOutput;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    result << first < second;
    resultOutput << "L'operando " & a & " è ";
    if result then {
        resultOutput << resultOutput & " minore ";
    } else {
        resultOutput << resultOutput & " maggiore o uguale ";
    }
    resultOutput << resultOutput & " di " & b;

    (resultOutput) -->!;
}

def handleLe(): void {
    float first;
    float second;
    boolean result;
    string resultOutput;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    result << first <= second;
    resultOutput << "L'operando " & a & " è ";
    if result then {
        resultOutput << resultOutput & " minore o uguale ";
    } else {
        resultOutput << resultOutput & " maggiore ";
    }
    resultOutput << resultOutput & " di " & b;

    (resultOutput) -->!;
}

def handleEq(): void {
    float first;
    float second;
    boolean result;
    string resultOutput;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    result << first = second;
    resultOutput << "L'operando " & a & " è ";
    if result then {
        resultOutput << resultOutput & " uguale ";
    } else {
        resultOutput << resultOutput & " diverso ";
    }
    resultOutput << resultOutput & " rispetto a " & b;

    (resultOutput) -->!;
}

def handleNe(): void {
    float first;
    float second;
    boolean result;
    string resultOutput;
    boolean success << getRealOperands(first, second);
    if not success then {
        return;
    }

    result << first != second;
    resultOutput << "L'operando " & a & " è ";
    if result then {
        resultOutput << resultOutput & " diverso ";
    } else {
        resultOutput << resultOutput & " uguale ";
    }
    resultOutput << resultOutput & " rispetto a " & b;

    (resultOutput) -->!;
}
