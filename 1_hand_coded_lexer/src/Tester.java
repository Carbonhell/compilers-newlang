package src;

public class Tester {

	public static void main(String[] args) {
		Lexer lexicalAnalyzer = new Lexer();
		for (String filePath : args) {
			System.out.println("Analyzing file: " + filePath);
			if (lexicalAnalyzer.initialize(filePath)) {
				Token token;
				try {
					while ((token = lexicalAnalyzer.nextToken()) != null) {
						System.out.println(token);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("File not found: " + filePath);
			}
		}
	}

}
