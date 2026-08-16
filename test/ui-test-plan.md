# Bambolino UI Test Plan

Expected output in each test starts after Bambolino's startup greeting. The generated session record contains the commands sent to the program and the output it produced.

## Test: list displays incomplete tasks

**Aim:** Verify that new tasks appear in the list with an incomplete status icon.

**Inputs:**
```text
read book
return book
buy bread
list
bye
```

**Expected output:**
```text
added:read book
added:return book
added:buy bread
____________________________________________________________
Here are the tasks in your list:
1.[ ] read book
2.[ ] return book
3.[ ] buy bread
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: mark marks a task as done

**Aim:** Verify that marking a task changes its status to done and confirms the selected task.

**Inputs:**
```text
read book
return book
mark 2
bye
```

**Expected output:**
```text
added:read book
added:return book
____________________________________________________________
Nice! I've marked this task as done:
  [X] return book
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: unmark changes a task back to incomplete

**Aim:** Verify that unmarking a completed task changes its status back to incomplete.

**Inputs:**
```text
read book
mark 1
unmark 1
bye
```

**Expected output:**
```text
added:read book
____________________________________________________________
Nice! I've marked this task as done:
  [X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [ ] read book
____________________________________________________________
Bye. Hope to see you again soon!
```
