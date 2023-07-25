package lambdaparser;

public class Token {
  public enum TokenType {
    lparen,
    rparen,
    lambda,
    dot,
    var,
    cons,
    succ,
    whitespace
  }

  TokenType type;
  char name;
  int value;

  public Token(TokenType t, char n) {
    type = t;
    name = n;
  }

  public Token(TokenType t, int v) {
    type = t;
    value = v;
  }

  public Token(TokenType t) {
    type = t;
    name = '-';
  }

  public TokenType getType() {
    return type;
  }

  public void setType(TokenType new_type) {
    this.type = new_type;
  }

  public char getName() {
    return name;
  }

  public void setName(char new_name) {
    this.name = new_name;
  }

  public int getValue() {
    return value;
  }

  public void setName(int new_value) {
    this.value = new_value;
  }
}
