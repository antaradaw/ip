# Bambolino UI Test Plan

Expected output in each test starts after Bambolino's startup greeting. The generated session record contains the commands sent to the program and the output it produced.

## Test: todo tasks display in the list

**Aim:** Verify that a to-do task is added and listed with its task type and incomplete status icon.

**Inputs:**
```text
todo borrow book
list
bye
```

**Expected output:**
```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: deadline parses and formats an ISO date

**Aim:** Verify that a deadline stores an ISO date and displays it in the user-friendly format.

**Inputs:**
```text
deadline return book /by 2019-10-15
list
bye
```

**Expected output:**
```text
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Oct 15 2019)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: deadline rejects an invalid date

**Aim:** Verify that a deadline date must use the yyyy-mm-dd format and be a real calendar date.

**Inputs:**
```text
deadline return book /by 2019-02-30
list
bye
```

**Expected output:**
```text
____________________________________________________________
Sorry, deadline dates must use yyyy-mm-dd. Try: deadline return book /by 2019-10-15
____________________________________________________________
No tasks added yet.
Bye. Hope to see you again soon!
```

## Test: delete removes a task and renumbers the list

**Aim:** Verify that deleting a task confirms the removed item and shifts later tasks to fill its number.

**Inputs:**
```text
todo read book
deadline return book /by 2019-06-06
event project meeting /from Aug 6th 2pm /to 4pm
delete 2
list
bye
```

**Expected output:**
```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Jun 06 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][ ] return book (by: Jun 06 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: mark and unmark retain the task type

**Aim:** Verify that marking and unmarking a to-do task changes only its completion status.

**Inputs:**
```text
todo read book
mark 1
unmark 1
bye
```

**Expected output:**
```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
Bye. Hope to see you again soon!
```
