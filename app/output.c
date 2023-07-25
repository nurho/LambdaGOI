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

  // Application Top
  l1:  printf("App top\n"); pushLeft(); goto l4;
  // Application Left
  l2:  printf("App left\n"); pushRight(); goto l4;
  // Application Right
  l3:  printf("App right\n"); pop(); if (carry == LEFT) goto l000; else goto l11;

  // Abstraction Top
  l4:  printf("Abs top\n"); pop(); if (carry == LEFT) goto l7; else goto l6;
  // Abstraction Left
  l5:  printf("Abs left\n"); pushRight(); goto l3;
  // Abstraction Right
  l6:  printf("Abs right\n"); pushLeft(); goto l3;

  // Application Top
  l7:  printf("App top\n"); pushLeft(); goto l10;
  // Application Left
  l8:  printf("App left\n"); pushRight(); goto l10;
  // Application Right
  l9:  printf("App right\n"); pop(); if (carry == LEFT) goto l6; else {pushRight(); goto l10;}

  // Successor
  l10: pop(); if (carry == LEFT) { printf("Succ visit\n"); goto l8; } else {num++; printf("Succ increment\n"); goto l9;}

  // Constant
  l11: pushNum(4); printf("Push num\n"); goto l2; 


  l000: printf("Finished after %d stack operations\n",count);  if (num_set > 0) printf("Result: %d\n", num);
  return 0;
}