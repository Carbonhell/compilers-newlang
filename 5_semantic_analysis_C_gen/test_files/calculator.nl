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
    OP_NE           << 15,
    OP_FIB          << 16;

def printIntro(): void {
    ("Calculator.nl") -->!;
    ("Operazioni disponibili:") -->!;
    ("\t") -->;
	(OP_ADD) -->;
	(":  Addizione") -->!;

    ("\t") -->;
	(OP_SUB) -->;
	(":  Sottrazione") -->!;

    ("\t") -->;
	(OP_MUL) -->;
	(":  Moltiplicazione") -->!;

    ("\t") -->;
	(OP_DIV) -->;
	(":  Divisione") -->!;

    ("\t") -->;
	(OP_POW) -->;
	(":  Potenza") -->!;

    ("\t") -->;
	(OP_STR_CONCAT) -->;
	(":  Concatenazione di stringhe") -->!;

    ("\t") -->;
	(OP_OR) -->;
	(":  OR logico") -->!;

    ("\t") -->;
	(OP_AND) -->;
	(":  AND logico") -->!;

    ("\t") -->;
	(OP_NOT) -->;
	(":  NOT logico") -->!;

    ("\t") -->;
	(OP_GT) -->;
	(":  Maggiore di") -->!;

    ("\t") -->;
	(OP_GE) -->;
	(":  Maggiore di o uguale") -->!;

    ("\t") -->;
	(OP_LT) -->;
	(":  Minore di") -->!;

    ("\t") -->;
	(OP_LE) -->;
	(":  Minore di o uguale") -->!;

    ("\t") -->;
	(OP_EQ) -->;
	(":  Uguaglianza") -->!;

    ("\t") -->;
	(OP_NE) -->;
	(":  Disuguaglianza") -->!;

	("\t") -->;
    (OP_FIB) -->;
    (":  Successione di Fibonacci") -->!;

    ("") -->!;

    ("\t") -->;
	(OP_STOP) -->;
	(":  Chiudi il programma") -->!;
}

start:
def main(): void {
    integer op;
    printIntro();
    op <-- "Specifica il prossimo tipo di operazione da eseguire, o inserisci '0' per terminare il programma: ";

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
        if op = OP_FIB then {
            handleFib();
        }

        op <-- "Specifica il prossimo tipo di operazione da eseguire, o inserisci '0' per terminare il programma: ";
    }

    ("Arrivederci!") -->!;
}

|*
    Writes a single user input in the out param, checking if the user wants to return to the menu.
    The operand out param is only valid if the return value is equal to true.
|
def getRealOperand(out float operand): boolean {
    operand <-- "Specifica il valore del prossimo operando: ";
    return true;
}

def getStringOperand(out string operand): boolean {
    operand <-- "Specifica il valore del prossimo operando: ";
    return true;
}

def getBooleanOperand(out boolean operand): boolean {
    operand <-- "Specifica il valore del prossimo operando (y o n): ";
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
    boolean success << getBooleanOperand(first);
    if not success then {
        return false;
    }
    success << getBooleanOperand(second);
    return success;
}

def printResult(string operation | float first, second, output): void {
    ("L'operazione di " & operation & " tra ") -->;
    (first) -->;
    (" e ") -->;
    (second) -->;
    (" ha come risultato: ") -->;
    (output) -->!;
}

def printResultComparison(string operation | float first, second | string output): void {
    ("L'operazione di " & operation & " tra ") -->;
    (first) -->;
    (" e ") -->;
    (second) -->;
    (" ha come risultato: " & output) -->!;
}

def printResultStrConcat(string operation, first, second, output): void {
    ("L'operazione di " & operation & " tra ") -->;
    (first) -->;
    (" e ") -->;
    (second) -->;
    (" ha come risultato: ") -->;
    (output) -->!;
}

def printResultBools(string operation | boolean first, second, output): void {
    ("L'operazione di " & operation & " tra ") -->;
    (first) -->;
    (" e ") -->;
    (second) -->;
    (" ha come risultato: ") -->;
    (output) -->!;
}

def printResultBool(string operation | boolean operand, output): void {
    ("L'operazione di " & operation & " su ") -->;
    (operand) -->;
    (" ha come risultato: ") -->;
    (output) -->!;
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
    printResult("somma", first, second, output);
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
    printResult("sottrazione", first, second, output);
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
    printResult("moltiplicazione", first, second, output);
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
    printResult("divisione", first, second, output);
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
    printResult("potenza", first, second, output);
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
    printResultStrConcat("concatenazione di stringhe", first, second, output);
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
    printResultBools("OR", first, second, output);
}

def handleAnd(): void {
    boolean first;
    boolean second;
    boolean output;
    boolean success << getBooleanOperands(first, second);
    if not success then {
        return;
    }

    output << first and second;
    printResultBools("AND", first, second, output);
}

def handleNot(): void {
    boolean operand;
    boolean output;
    boolean success << getBooleanOperand(operand);
    if not success then {
        return;
    }

    output << not operand;

    printResultBool("NOT", operand, output);
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
    if result then {
        resultOutput << "maggiore";
    } else {
        resultOutput << "minore o uguale";
    }

    printResultComparison(">", first, second, resultOutput);
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
    if result then {
        resultOutput << "maggiore o uguale";
    } else {
        resultOutput << "minore";
    }

    printResultComparison(">=", first, second, resultOutput);
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
    if result then {
        resultOutput << "minore";
    } else {
        resultOutput << "maggiore o uguale";
    }
    printResultComparison("<", first, second, resultOutput);
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
    if result then {
        resultOutput << "minore o uguale";
    } else {
        resultOutput << "maggiore";
    }

    printResultComparison("<=", first, second, resultOutput);
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
    if result then {
        resultOutput << "uguale";
    } else {
        resultOutput << "diverso";
    }

    printResultComparison("=", first, second, resultOutput);
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
    if result then {
        resultOutput << "diverso";
    } else {
        resultOutput << "uguale";
    }

    printResultComparison("<>", first, second, resultOutput);
}

def handleFib(): void {
    integer n, penultimo << 0, ultimo << 1, f;
    n <-- "Specifica il numero (>1) di elementi della successione di Fibonacci da visualizzare: ";

    if n < 2 then {
        return;
    } else {
        (penultimo) -->!;
        (ultimo) -->!;
        n << n - 2;
        while n > 0 loop {
            f << ultimo + penultimo;
            (f) -->!;
            penultimo << ultimo;
            ultimo << f;
            n << n - 1;
        }
    }
}