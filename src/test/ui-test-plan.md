# UI Test Plan

The executable JUnit tests are in `src/test/java/UiTest.java`. Each case below records its aim, console input, and exact expected output.

## Test case: Exit the application

**Aim:** Verify that the application shows its welcome message and exits politely when the user enters `bye`.

### Input
```text
bye
```

### Expected output
```text
Hello! I'm Tard_T. 
What can I do for you? 
________________________________

________________________________
Bye. Hope to see you again soon!
________________________________
```

## Test case: Add and list a to-do task

**Aim:** Verify that a `todo` command adds a task and that `list` displays the task with its to-do status.

### Input
```text
todo read book
list
bye
```

### Expected output
```text
Hello! I'm Tard_T. 
What can I do for you? 
________________________________

________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
________________________________
________________________________
1. [T][ ] read book
________________________________
________________________________
Bye. Hope to see you again soon!
________________________________
```
