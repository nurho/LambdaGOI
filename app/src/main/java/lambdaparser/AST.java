package lambdaparser;

import java.util.ArrayList;

public class AST {
  Boolean is_root;

  Node root;
  AST parent;
  ArrayList<AST> children;
  //String label;

  public String label_top;
  public String label_left;
  public String label_right;

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

  public void writeLabels(String parent_top, String parent_left, String parent_right) {
    /*
    if (root.getTypeName() == "Var") {
      App.output_code += "// Variable\n" + label_Top + ": " 
                      + "goto " + parent_label + ";\n\n";

    } else*/ if (root.getTypeName() == "Cons") {
      App.output_code += "  // Constant\n  " + label_top + ": " 
      + "pushNum(" + root.getValue() + "); printf(\"Push num\\n\");" + " goto "
      + parent_left + "; " + "\n\n";

    } else if (root.getTypeName() == "Succ") {
      App.output_code += "  // Successor\n  " + label_top + ": " 
      + "pop(); if (carry == LEFT) { printf(\"Succ visit\\n\"); goto " + parent_left
      + "; } else {num++; pushLeft(); printf(\"Succ increment\\n\"); goto " + parent_right + ";}\n\n";


      /* else if (root.getTypeName() == "Succ") {
      App.output_code += "  // Successor\n  " + label_top + ": " 
      + "pop(); if (carry == LEFT) {pushRight(); goto " + parent_right
      + ";} else {num_stack[0]++; pushLeft(); goto " + parent_right + ";}\n\n";

*/
    } else if (root.getTypeName() == "Abs") {
      String child_left = children.get(0).label_top;
      String child_right = children.get(1).label_top;

      if (children.get(1).root.getTypeName() == "Var") {
        App.output_code += "  // Abstraction Top\n  " + label_top + ": " 
        + " printf(\"Abs top\\n\"); pop(); if (carry == LEFT) goto " + label_left
        + "; else goto " + label_right + ";\n";
      } else {
        App.output_code += "  // Abstraction Top\n  " + label_top + ": " 
        //+ "pop(); if (carry == LEFT) goto " + label_left
        + " printf(\"Abs top\\n\"); pop(); if (carry == LEFT) goto " + child_right
        //+ "; else goto " + child_right + ";\n";
        + "; else goto " + label_right + ";\n";
      }
      App.output_code += "  // Abstraction Left\n  " + label_left + ": " 
      + " printf(\"Abs left\\n\"); pushRight(); goto " + parent_right + ";\n";
      App.output_code += "  // Abstraction Right\n  " + label_right + ": " 
      + " printf(\"Abs right\\n\"); pushLeft(); goto " + parent_right + ";\n\n";

      children.get(0).writeLabels(label_top, label_left, label_right);
      children.get(1).writeLabels(label_top, label_left, label_right);

    } else if (root.getTypeName() == "App") {
      String child_left = children.get(0).label_top;
      String child_right = children.get(1).label_top;

      App.output_code += "  // Application Top\n  " + label_top + ": " 
      + " printf(\"App top\\n\"); pushLeft(); goto "+ child_left + ";\n";

      App.output_code += "  // Application Left\n  " + label_left + ": " 
      + " printf(\"App left\\n\"); pushRight(); goto " + child_left + ";\n";

      if (children.get(1).root.getTypeName() == "Var") {
        App.output_code += "  // Application Right\n  " + label_right + ": " 
        + " printf(\"App right\\n\"); pop(); if (carry == LEFT) goto " + parent_right 
        + "; else {pushRight(); goto " + child_left + ";}\n\n";
      } else {
        App.output_code += "  // Application Right\n  " + label_right + ": " 
        + " printf(\"App right\\n\"); pop(); if (carry == LEFT) goto " + parent_top 
        + "; else goto " + child_right + ";\n\n";
      }

      children.get(0).writeLabels(label_top, label_left, label_right);
      children.get(1).writeLabels(label_top, label_left, label_right);
    }
    /*
    if (root.getTypeName() == "Var") {
      App.output_code += "// Variable\n" + label_Top + ":\n" 
                      + "// Return to parent\n" + "Jump to "
                      + parent_label + "\n\n";
    } else if (root.getTypeName() == "Cons") {
      App.output_code += "// Constant\n" + label_Top + ":\n" 
                      + "// Put Constant in stack\n" + "Jump to "
                      + parent_label + "\n\n";
    } else if (root.getTypeName() == "Abs") {
      App.output_code += "// Abstraction Top\n" + label_Top + ":\n" 
                      + "// IF came from Parent\n" + "Jump to "
                      + label_Left + "\n"
                      + "// IF came from Right\n" + "Jump to "
                      + parent_label + "\n\n";

      App.output_code += "// Abstraction Left\n" + label_Left + ":\n" 
                      + "// IF came from Top\n" + "Jump to "
                      + children.get(0).label_Top + "\n"
                      + "// IF came from Child\n" + "Jump to "
                      + label_Right + "\n\n";

      App.output_code += "// Abstraction Right\n" + label_Top + ":\n" 
                      + "// IF came from Left\n" + "Jump to "
                      + children.get(1).label_Top + "\n"
                      + "// IF came from Child\n" + "Jump to "
                      + label_Top + "\n\n";

      children.get(0).writeLabels(label_Left);
      children.get(1).writeLabels(label_Right);
    } else if (root.getTypeName() == "App") {
      App.output_code += "// Application Top\n" + label_Top + ":\n" 
                      + "// IF came from Parent\n" + "Jump to "
                      + label_Right + "\n"
                      + "// IF came from Left\n" + "Jump to "
                      + parent_label + "\n\n";

      App.output_code += "// Application Left\n" + label_Left + ":\n" 
                      + "// IF came from Right\n" + "Jump to "
                      + children.get(0).label_Top + "\n"
                      + "// IF came from Child\n" + "Jump to "
                      + label_Top + "\n\n";

      App.output_code += "// Application Right\n" + label_Top + ":\n" 
                      + "// IF came from Top\n" + "Jump to "
                      + children.get(1).label_Top + "\n"
                      + "// IF came from Child\n" + "Jump to "
                      + label_Left + "\n\n";

      children.get(0).writeLabels(label_Left);
      children.get(1).writeLabels(label_Right);
    }
    */
  }

  public void generateLabels() {
    if (root.getTypeName() != "Var") {
      label_top = "l" + App.label_number;
      App.label_number++;
      if (root.getTypeName() == "App" || root.getTypeName() == "Abs") {
        label_left = "l" + App.label_number;
        App.label_number++;
        label_right = "l" + App.label_number;
        App.label_number++;

        for (int i = 0; i < children.size(); i++) {
          children.get(i).generateLabels();
        }
      }
    }
  }
}
