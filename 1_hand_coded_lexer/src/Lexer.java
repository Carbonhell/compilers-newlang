package src;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Lexer {

	private BufferedReader input;
	private static HashMap<String, Token> stringTable;  // la struttura dati potrebbe essere una hash map
	private State state;

	public enum State {
		START,
		LETTER, // identifier or keyword
		NUMBER,
		FLOATSTART, // number with dot
		FLOAT,
		LESS,
		GREATER,
		EQUAL,
		ARROW_START,
	}

	public Lexer() {
		// la symbol table in questo caso la chiamiamo stringTable
		stringTable = new HashMap<>();
		state = State.START;
		stringTable.put("if", new Token(Token.T_IF));   // inserimento delle parole chiavi nella stringTable per evitare di scrivere un diagramma di transizione per ciascuna di esse (le parole chiavi verranno "catturate" dal diagramma di transizione e gestite e di conseguenza). IF poteva anche essere associato ad una costante numerica
		stringTable.put("then", new Token(Token.T_THEN));
		stringTable.put("else", new Token(Token.T_ELSE));
		stringTable.put("while", new Token(Token.T_WHILE));
		stringTable.put("int", new Token(Token.T_INT));
		stringTable.put("float", new Token(Token.T_FLOAT));
	}

	public Boolean initialize(String filePath) {
		// prepara file input per lettura e controlla errori
		try {
			input = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filePath),
							StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;

	}

	public Token nextToken() throws Exception {

		//Ad ogni chiamata del lexer (nextToken())
		//si resettano tutte le variabili utilizzate
		state = State.START;
		StringBuilder lessema = new StringBuilder(); // il lessema riconosciuto
		char c;

		while (true) {
			int inputChar = input.read();
			c = (inputChar == -1) ? ' ' : (char) inputChar; // If EOF is detected, use an empty space to get the last token processed.

			switch (state) {
				case START:
					if (inputChar != -1 && Character.isWhitespace(c)) {
						continue;
					} else if (Character.isLetter(c)) {
						lessema.append(c);
						state = State.LETTER;
						break;
					} else if (Character.isDigit(c)) {
						lessema.append(c);
						state = State.NUMBER;
						break;
					}

					// Separators and operators
					switch (c) {
						case '{':
							return new Token(Token.T_LCURLYBRACKET);
						case '}':
							return new Token(Token.T_RCURLYBRACKET);
						case '(':
							return new Token(Token.T_LBRACKET);
						case ')':
							return new Token(Token.T_RBRACKET);
						case ';':
							return new Token(Token.T_SEMICOLON);
						case ',':
							return new Token(Token.T_COMMA);
						case '<':
							lessema.append(c);
							state = State.LESS;
							break;
						case '>':
							lessema.append(c);
							state = State.GREATER;
							break;
						case '=':
							lessema.append(c);
							state = State.EQUAL;
							break;
						case '+':
							return new Token(Token.T_PLUS);
						case '-':
							return new Token(Token.T_MINUS);
						default:
							if (inputChar != -1) {
								lessema.append(c);
								return new Token(Token.T_ERROR, lessema.toString());
							}
					}

					break;

				case LETTER:
					if (Character.isLetterOrDigit(c)) {
						lessema.append(c);
						break;
					} else {
						retrack();
						// case sensitive!
						// if the lexeme is a reserved keyword, return the associated token
						// else return the token associated to the identifier and create it if it doesn't exist
						return installID(lessema.toString());
					}

				case NUMBER:
					if (Character.isDigit(c)) {
						lessema.append(c);
					} else if (c == '.') {
						lessema.append(c);
						state = State.FLOATSTART;
					} else {
						retrack();
						return new Token(Token.T_NUMBER, lessema.toString());
					}
					break;

				// "2." is accepted, by converting it to 2.0
				case FLOATSTART:
					if (Character.isDigit(c)) {
						lessema.append(c);
						state = State.FLOAT;
					} else {
						lessema.append('0');
						retrack();
						return new Token(Token.T_NUMBER, lessema.toString());
					}
					break;

				case FLOAT:
					if (Character.isDigit(c)) {
						lessema.append(c);
					} else {
						retrack();
						return new Token(Token.T_NUMBER, lessema.toString());
					}
					break;

				case LESS:
					if (c == '=') {
						return new Token(Token.T_LESSOREQUAL);
					} else if (c == '-') {
						lessema.append(c);
						state = State.ARROW_START;
						break;
					} else if (c == '>') {
						return new Token(Token.T_NOTEQUAL);
					} else {
						retrack();
						return new Token(Token.T_LESS);
					}

				case GREATER:
					if (c == '=') {
						return new Token(Token.T_GREATEROREQUAL);
					} else {
						retrack();
						return new Token(Token.T_GREATER);
					}

				case EQUAL:
					if (c == '=') {
						return new Token(Token.T_EQUAL);
					} else {
						retrack();
						return new Token(Token.T_ERROR, lessema.toString());
					}

				case ARROW_START:
					if (c == '-') {
						return new Token(Token.T_ASSIGN);
					} else {
						retrack();
						return new Token(Token.T_ERROR, lessema.toString());
					}
			}
			// Checked at the end to let the last lexeme get processed
			if (inputChar == -1) {
				return null;
			}
			setRetrackMark(); // So that we can retract if the current token ends in the next character
		}
	}

	private Token installID(String lessema) {
		//utilizzo come chiave della hashmap il lessema
		if (stringTable.containsKey(lessema))
			return stringTable.get(lessema);
		else {
			Token token = new Token(Token.T_ID, lessema);
			stringTable.put(lessema, token);
			return token;
		}
	}


	private void retrack() throws IOException {
		// fa il retract nel file di un carattere
		input.reset();
	}

	private void setRetrackMark() throws IOException {
		input.mark(1);
	}
}