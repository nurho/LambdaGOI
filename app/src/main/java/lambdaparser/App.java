package lambdaparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


class App {
  public static String output_code = "";
  public static int label_number = 1;
  public static Boolean root_set = false;

  public static void main (String[] args) throws IOException{
    System.out.println(" --- Lambda Parser --- ");
    System.out.println("Enter program:");

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    String input = reader.readLine();

    Lexer lexer = new Lexer();
    ArrayList<Token> tokens = lexer.lex(input);

    System.out.println("\nTokens:");
    for (int i = 0; i < tokens.size(); i++) {
      Token t = tokens.get(i);
      if (t.getType().name() == "cons") {
        System.out.println(t.getType().name() + ": " + t.getValue());
      } else {
        System.out.println(t.getType().name() + ": " + t.getName());
      }
    }

    Parser parser = new Parser();
    AST ast;

    try {
      ast = parser.parse(tokens);
    } catch (Exception e) {
      System.err.println("Parse error in App.java");
      return;
    }

    System.out.println("\nTree:");
    ast.printAST();

    System.out.println("\nGenerating labels...\n");
    ast.generateLabels();

    Path boilerplate = Paths.get("src/main/resources/boilerplate.txt");
    String final_code = Files.readString(boilerplate);

    System.out.println("\nOutput:");
    ast.writeLabels("l000","","");
    System.out.print(output_code);

    final_code += output_code;
    final_code += "\n  l000: printf(\"Finished after %d stack operations\\n\",count);"
                + "  if (num_set > 0) printf(\"Result: %d\\n\", num);"
                + "\n  return 0;\n}";
    PrintWriter out = new PrintWriter("output.c");
    out.print(final_code);
    out.close();
  }
}
