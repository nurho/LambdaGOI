package lambdaparser;

import java.util.ArrayList; 

class Parser {

  ArrayList<Token> tokens;
  int current;

  AST parse(ArrayList<Token> t) {
    tokens = t;
    current = 0;
    return parseTerm();
  } 

  AST parseTerm() {
    return parseApp();
  }

  AST parseApp() {

    AST term;
    term = parseAtom();
    //while (current < tokens.size() && tokens.get(current).getType() == Token.TokenType.whitespace) {
    // current++;
    term = parseAppPrime(term);
    //}
    return term;
  }

  AST parseAppPrime(AST term) {
    if (current < tokens.size()) {
      Token tok = tokens.get(current);
      if (tok.getType() == Token.TokenType.lparen || tok.getType() == Token.TokenType.var || tok.getType() == Token.TokenType.cons) {
        AST right = parseAtom();
        AST app = new AST(new Node(Node.NodeType.app_node));
        app.addChild(term);
        app.addChild(right);
        return parseAppPrime(app);
      }
    }
    return term;
  }

  AST parseAtom() {
    try{
    Token tok = tokens.get(current);
    if (tok.getType() == Token.TokenType.lparen) {
      current++;
      AST term = parseTerm();
      if (tokens.get(current).getType() != Token.TokenType.rparen) {
        throw new Exception("Missing right parenthesis");
      }
      current++;
      return term;
    } else if (tok.getType() == Token.TokenType.var) {
      current++;
      AST var = new AST(new Node(Node.NodeType.var_node, tok.getName()));
      return var;
    }  else if (tok.getType() == Token.TokenType.cons) {
      current++;
      AST cons = new AST(new Node(Node.NodeType.cons_node, tok.getValue()));
      return cons;
    }  else if (tok.getType() == Token.TokenType.succ) {
      current++;
      AST succ = new AST(new Node(Node.NodeType.succ_node));
      return succ;
    } else if (tok.getType() == Token.TokenType.lambda) {
      current++;
      if (tokens.get(current).getType() != Token.TokenType.var) {
        throw new Exception("Variable error");
      }
      AST var = new AST(new Node(Node.NodeType.var_node, tokens.get(current).getName()));

      current++;
      if (tokens.get(current).getType() != Token.TokenType.dot) {
        throw new Exception("Missing dot");
      }

      current++;
      AST body = parseTerm();
      AST abs = new AST(new Node(Node.NodeType.abs_node));
      abs.addChild(var);
      abs.addChild(body);
      return abs;

      
    } else {
      throw new Exception("Invalid atom");
    }
    } catch (Exception e) {
      System.err.println("Parsing error in Parser.java");
      return null;
    }
  }

}
