# Assembly
## How the parser creates assembly labels:

### Application
Application has an entry port and a port each for the argument expression and the 
function expression:

>    |
>    1
>   (@)
>   2 3
>  /   \

1. Top
2. Left
3. Right


### Abstraction
An abstraction has the same structure except this time the left port represents the 
bound variable and the right port represents the body of the abstraction:

>    |
>    1
>   (\)
>   2 3
>  /   \

1. Top
2. Left
3. Right

### Variables
A variable node just has one port where the token enters, retrieves the variable then 
exits again:

>    |
>    1
>   (x) 

1. Variable


## Method
The parser must traverse the tree (BFS or DFS?) and decide how to create assembly language labels out of it.
We must consider that each port on a node can be a label. A variable node has one port, application and abstraction have 3.

Application has 3 blocks of code
Abstraction has 3 blocks of code
Variable has 1 block of code

What is different when the code is generated is that the jump locations are different.
You can traverse the tree and create a lookup table for the connections:

|L1|L2|
|L3|L4|
|L5|L7|
etc.

### Example
```c
l1: PUSHLEFT; goto l4;                                  // App Top
l2: PUSHRIGHT; goto l4;                                 // App Left
l3: POP; if (carry == LEFT)  goto l000; else goto l7;   // App Right

l4: POP; if (carry == LEFT)  goto l6; else goto l5;     // Abs Top
l5: PUSHLEFT; goto  l3;                                 // Abs Left
l6: PUSHRIGHT; goto l3;                                 // Abs Right

l7: Pushnum(4); goto l2;                                // Cons

l000: printf("Finished after %d stack operations\n",count);
```


## Labels

### Application
// Application Top
lxx: PUSHLEFT; goto #LeftChild#;

// Application Left
lxx: PUSHRIGHT; goto #LeftChild#;

// Application Right
lxx: POP; if (carry == LEFT) goto #Parent#; else goto #RightChild#;

### Abstraction
// Abstraction top
lxx: POP; if (carry == LEFT) goto #RightChild#; else goto #LeftChild#; 

// Abstraction Left
lxx: PUSHLEFT; goto #ParentRight#

// Abstraction Right
lxx: PUSHRIGHT; goto #ParentRight#

### Variables
???

### Num
lxx: pushNum(x); goto #ParentLeft#;

