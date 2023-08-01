#include <stdio.h>

// Defines
#define RIGHT 0
#define LEFT  1

// Stack
int mult = 0;
int carry;
int count = 0;

// Number
int num = 0;
short num_set = 0;


void pushLeft() {
  mult = (mult<<1); mult++;
  count++;
}

void pushRight() {
  mult = (mult<<1);
  count++;
}

void pop () {
  if (mult % 2 == 0) {
    carry = RIGHT;
    mult = (mult>>1);
  } else {
    carry = LEFT;
    mult = (mult>>1);
  }
  count++;
}

void pushNum(int n) {
  num = n;
  num_set = 1;
  count++;
}

int main(int argc, char *argv[]) {
  // Generated code goes here

  // Abstraction Top
  l1:  printf("Abs top\n"); pop(); if (carry == LEFT) goto l4; else goto l5;
  // Abstraction Left
  l2:  printf("Abs left\n"); pushRight(); goto ;
  // Abstraction Right
  l3:  printf("Abs right\n"); pushLeft(); goto ;

  // Application Top
  l4:  printf("App top\n"); pushLeft(); goto l7;
  // Application Right
  l6:  printf("App right\n"); pushRight(); goto l7;
  // Application left
  l5:  printf("App left\n"); pop(); if (carry == LEFT) goto l1; else goto l9;

  // Successor top
  l7:  printf("Succ visit\n"); goto l5;

  // Successor bottom
  l8: printf("Succ increment\n");  num++; goto l6;

  // Application Top
  l9:  printf("App top\n"); pushLeft(); goto l12;
  // Application Right
  l11:  printf("App right\n"); pushRight(); goto l12;
  // Application left
  l10:  printf("App left\n"); pop(); if (carry == LEFT) goto l4; else goto l17;

  // Abstraction Top
  l12:  printf("Abs top\n"); pop(); if (carry == LEFT) goto l15; else goto l16;
  // Abstraction Left
  l13:  printf("Abs left\n"); pushRight(); goto l10;
  // Abstraction Right
  l14:  printf("Abs right\n"); pushLeft(); goto l10;

  // Successor top
  l15:  printf("Succ visit\n"); goto l13;

  // Successor bottom
  l16: printf("Succ increment\n");  num++; goto l14;

  // Constant
  l17: pushNum(4); printf("Push num\n"); num_set = 1; goto l11; 


  l000: printf("Finished after %d stack operations\n",count);  if (num_set > 0) printf("Result: %d\n", num);
  return 0;
}