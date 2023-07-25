package lambdaparser;

import java.util.ArrayList;

public class Lexer {

  public ArrayList<Token> lex(String input) {
    ArrayList<Token> tokens = new ArrayList<Token>();

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);

      if (c == '(') {
        tokens.add(new Token(Token.TokenType.lparen));
      } else if (c == ')') {
        tokens.add(new Token(Token.TokenType.rparen));
      } else if (c == '\\') {
        tokens.add(new Token(Token.TokenType.lambda));
      } else if (c == '.') {
        tokens.add(new Token(Token.TokenType.dot));
      } else if (c >= 'a' && c <= 'z') {
        tokens.add(new Token(Token.TokenType.var, c));
      } else if (c == 'S') {
        tokens.add(new Token(Token.TokenType.succ));
      } else if (c >= '0' && c <= '9') {
        String s = "" + c;
        c = input.charAt(i+1);
        while (c >= '0' && c <= '9') {
          s += c;
          i++;
          c = input.charAt(i+1);
        }
        tokens.add(new Token(Token.TokenType.cons, Integer.parseInt(s)));
      } else if (c == ' ' || c == '\t' || c == '\n') {
        tokens.add(new Token(Token.TokenType.whitespace));
      }
    }

    return tokens;
  }
}
