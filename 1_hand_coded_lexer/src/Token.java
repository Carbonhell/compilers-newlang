package src;

public class Token {
	public final static String T_ERROR = "ERROR";
	public final static String T_IF = "IF";
	public final static String T_THEN = "THEN";
	public final static String T_ELSE = "ELSE";
	public final static String T_WHILE = "WHILE";
	public final static String T_INT = "INT";
	public final static String T_FLOAT = "FLOAT";
	public final static String T_LCURLYBRACKET = "LCURLYBRACKET";
	public final static String T_RCURLYBRACKET = "RCURLYBRACKET";
	public final static String T_LBRACKET = "LBRACKET";
	public final static String T_RBRACKET = "RBRACKET";
	public final static String T_SEMICOLON = "SEMICOLON";
	public final static String T_COMMA = "COMMA";
	public final static String T_NUMBER = "NUMBER";
	public final static String T_PLUS = "PLUS";
	public final static String T_MINUS = "MINUS";
	public final static String T_LESSOREQUAL = "LESSOREQUAL";
	public final static String T_NOTEQUAL = "NOTEQUAL";
	public final static String T_LESS = "LESS";
	public final static String T_GREATEROREQUAL = "GREATEROREQUAL";
	public final static String T_GREATER = "GREATER";
	public final static String T_EQUAL = "EQUAL";
	public final static String T_ASSIGN = "ASSIGN";
	public final static String T_ID = "ID";


	private String name;     // questo è un identificativo di token: potrebbe anche essere un intero
	private String attribute;

	public Token(String name, String attribute){
		this.name = name;
		this.attribute = attribute;
	}
	
	public Token(String name){
		this.name = name;
		this.attribute = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public String toString(){
		return attribute==null? name : "("+name+", \""+attribute+"\")";
				
	}
}
