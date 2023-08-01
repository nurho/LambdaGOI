package lambdaparser;

import java.util.ArrayList;
import java.util.HashMap;

public class AST {
  Boolean is_root;

  Node root;
  AST parent;
  ArrayList<AST> children;

  public String label_top;
  public String label_left;
  public String label_right;

  public String label_top_out;
  public String label_top_out2;
  public String label_left_out;
  public String label_left_out2;
  public String label_right_out;

  public static HashMap<Character, AST> vars;

  public AST(Node r) {
    this.root = r;
    this.children = new ArrayList<AST>();
  }

  public void addChild(AST a) {
    children.add(a);
  }

  public void printAST() {
    System.out.println(root.toString());
    for (int i = 0; i < children.size(); i++) {
      children.get(i).printAST();
    }
  }

  public void generateLabels() {
    if (root.getTypeName() != "Var") {
      label_top = "l" + App.label_number;
      App.label_number++;
      if (root.getTypeName() == "App" || root.getTypeName() == "Abs") {
        label_right = "l" + App.label_number;
        App.label_number++;
        label_left = "l" + App.label_number;
        App.label_number++;

        for (int i = 0; i < children.size(); i++) {
          children.get(i).generateLabels();
        }
      } else if (root.getTypeName() == "Succ") {
        label_left = "l" + App.label_number;
        App.label_number++;
      }
    }
  }
  
  public void getConnections(String caller) {
    // Constant
    if (root.getTypeName() == "Cons") {
      label_top_out = parent.label_top;

    // Successor
    } else if (root.getTypeName() == "Succ") {
      AST abs = vars.get(children.get(0).root.getName());
      label_top_out = caller;
      label_left_out = abs.label_left;
      abs.label_left_out = label_left;

    // Abstraction
    } else if (root.getTypeName() == "Abs") {
      label_top_out = caller;
      if(children.get(1).root.getTypeName() == "Var") {
        label_right_out = label_left;
        label_left_out = label_right;
      } else {
        label_right_out = children.get(1).label_top;
        vars.put(children.get(0).root.getName(), this);
        children.get(1).getConnections(label_right);
      }

    // Application
    } else if (root.getTypeName() == "App") {
      label_top_out = caller;
      if(children.get(0).root.getTypeName() == "Var") {
        AST abs = vars.get(children.get(0).root.getName());
        label_left_out = abs.label_left;
      } else {
        label_left_out = children.get(0).label_top;
        children.get(0).getConnections(label_left);
      }
      
      if(children.get(1).root.getTypeName() == "Var") {
        AST abs = vars.get(children.get(1).root.getName());
        label_right_out = abs.label_left;
      } else {
        label_right_out = children.get(1).label_top;
        children.get(1).getConnections(label_right);
      }
    }
  }

  public void writeLabels(String parent_top, String parent_left, String parent_right) {

    // Constant
    if (root.getTypeName() == "Cons") {
      App.output_code += "  // Constant\n  " + label_top + ": " 
      + "pushNum(" + root.getValue() + "); printf(\"Push num\\n\"); num_set = 1;"
      + " goto " + label_top_out + "; " + "\n\n";

    // Successor
    } else if (root.getTypeName() == "Succ") {
      App.output_code += "  // Successor top\n  " + label_top + ": " 
      + " printf(\"Succ visit\\n\"); goto " + label_left_out + ";\n\n";
      App.output_code += "  // Successor bottom\n  " + label_left + ": " 
      + "printf(\"Succ increment\\n\");  num++; goto " + label_top_out + ";\n\n";

    // Abstraction
    } else if (root.getTypeName() == "Abs") {
      String child_left = children.get(1).label_left;
      String child_right = children.get(1).label_top;

      if (children.get(1).root.getTypeName() == "Var") {
        App.output_code += "  // Abstraction Top\n  " + label_top + ": " 
        + " printf(\"Abs top\\n\"); pop(); if (carry == LEFT) goto " + label_left
        + "; else { goto " + label_right + ";}\n";
      } else {
        App.output_code += "  // Abstraction Top\n  " + label_top + ": " 
        + " printf(\"Abs top\\n\"); pop(); if (carry == LEFT) goto " + child_right
        + "; else goto " + child_left + ";\n";
      }
      App.output_code += "  // Abstraction Left\n  " + label_left + ": " 
      + " printf(\"Abs left\\n\"); pushRight(); goto " + parent_left + ";\n";
      App.output_code += "  // Abstraction Right\n  " + label_right + ": " 
      + " printf(\"Abs right\\n\"); pushLeft(); goto " + parent_left + ";\n\n";

      children.get(0).writeLabels(label_top, label_left, label_right);
      children.get(1).writeLabels(label_top, label_left, label_right);

    // Application
    } else if (root.getTypeName() == "App") {
      String child_left = children.get(0).label_top;
      String child_right = children.get(1).label_top;

      App.output_code += "  // Application Top\n  " + label_top + ": " 
      + " printf(\"App top\\n\"); pushLeft(); goto "+ child_left + ";\n";

      App.output_code += "  // Application Right\n  " + label_right + ": " 
      + " printf(\"App right\\n\"); pushRight(); goto " + child_left + ";\n";

      App.output_code += "  // Application left\n  " + label_left + ": " 
      + " printf(\"App left\\n\"); pop(); if (carry == LEFT) goto " + parent_top 
      + "; else goto " + child_right + ";\n\n";

      children.get(0).writeLabels(label_top, label_left, label_right);
      children.get(1).writeLabels(label_top, label_left, label_right);
    }
  }
}
