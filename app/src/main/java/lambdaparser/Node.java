package lambdaparser;

public class Node {
  public enum NodeType {
    var_node,
    cons_node,
    succ_node,
    abs_node,
    app_node
  }

  NodeType type;
  char name;
  int value;

  public Node(NodeType t) {
    this.type = t;
  }

  public Node(NodeType t, char n) {
    this.type = t;
    this.name = n;
  }

  public Node(NodeType t, int v) {
    this.type = t;
    this.value = v;
  }
  
  public int getValue() {
    return value;
  }
  
  public char getName() {
    return name;
  }

  public String getTypeName() {
    switch (type) {
      case var_node:
      return "Var";
      case cons_node:
      return "Cons";
      case succ_node:
      return "Succ";
      case abs_node:
      return "Abs";
      case app_node:
      return "App";
      default:
      return "NULL";
    }
  }

  public String toString() {
    switch(type) {
      case var_node:
      return "Var: " + name;
      case cons_node:
      return "Cons: " + value;
      case succ_node:
      return "Succ";
      case abs_node:
      return "Abs";
      case app_node:
      return "App";
      default:
      return "NULL";
    }
  }
}
