# Bambolino UI Test Plan

Expected output in each test starts after Bambolino's startup greeting. The generated session record contains the commands sent to the program and the output it produced.

## Test: command aliases behave like full command names

**Aim:** Verify that short, case-insensitive aliases execute the same actions as the corresponding full commands.

**Inputs:**
```text
T borrow book
L
Q
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

## Test: find searches task descriptions

**Aim:** Verify that find is case-insensitive, searches descriptions, and keeps the original task numbers.

**Inputs:**
```text
todo read book
deadline return book /by 2019-06-06
event project meeting /from Aug 6th 2pm /to 4pm
find BOOK
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
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Jun 06 2019)
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

## Test: find matches task descriptions without case sensitivity

**Aim:** Verify that find displays only tasks whose descriptions contain the keyword, regardless of letter case.

**Inputs:**
```text
todo borrow book
deadline return book /by 2019-10-15
event team meeting /from Mon 2pm /to 4pm
find BOOK
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
Got it. I've added this task:
  [D][ ] return book (by: Oct 15 2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] team meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: find reports an empty result

**Aim:** Verify that find reports when no task description contains the keyword.

**Inputs:**
```text
todo borrow book
find notebook
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
Here are the matching tasks in your list:
No matching tasks found.
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: find requires a keyword

**Aim:** Verify that find explains how to provide a missing keyword.

**Inputs:**
```text
find
bye
```

**Expected output:**
```text
____________________________________________________________
Sorry, the find command needs a keyword. Try: find book
____________________________________________________________
Bye. Hope to see you again soon!
```

## Manual GUI checks for A-BetterGui

These visual checks supplement the console regression cases above.

- Submit `todo read book` with Enter and `list` with Send: commands appear on the right;
  responses appear in wider white cards without console separator lines or repeated avatars.
- Submit `todo`: the reply has a COMMAND ERROR label and contrasting red styling. The input
  remains selected for correction. Replace it with `todo read notes`: the reply uses normal styling.
- Resize from 680 × 520 to the minimum 380 × 360 and then enlarge: long commands and replies
  wrap within the conversation; the input and Send button remain usable without horizontal scrolling.
- Add enough tasks to require scrolling, then use `list`: the full response can be read by scrolling.
- Empty or whitespace-only input disables Send. Sending a valid command clears and refocuses the input.

## Test: repeated and missing parameters do not add tasks

**Aim:** Verify invalid commands report errors without changing tasks or ending the session.

**Inputs:**
```text
deadline book /by 2026-09-13 /by 2026-09-14
deadline book /by
event meeting /from 2pm /from 3pm /to 4pm
event meeting /from 2pm /to 3pm /to 4pm
event /from 2pm /to 4pm
event meeting /to 4pm /from 2pm
list
bye
```

**Expected output:**
```text
____________________________________________________________
Sorry, use /by exactly once, separated by spaces.
____________________________________________________________
____________________________________________________________
Sorry, a deadline needs both a description and a date after /by.
____________________________________________________________
____________________________________________________________
Sorry, use /from exactly once, separated by spaces.
____________________________________________________________
____________________________________________________________
Sorry, use /to exactly once, separated by spaces.
____________________________________________________________
____________________________________________________________
Sorry, an event needs a description, a start after /from, and an end after /to.
____________________________________________________________
____________________________________________________________
Sorry, use /to exactly once, separated by spaces.
____________________________________________________________
No tasks added yet.
Bye. Hope to see you again soon!
```

## Test: invalid exit and task numbers allow recovery

**Aim:** Verify invalid commands report errors without changing tasks or ending the session.

**Inputs:**
```text
bye now
q now
mark 99999999999999999999
delete 0
unmark -1
list
bye
```

**Expected output:**
```text
____________________________________________________________
Sorry, the bye command does not take any extra words.
____________________________________________________________
____________________________________________________________
Sorry, the bye command does not take any extra words.
____________________________________________________________
____________________________________________________________
Sorry, the task number for mark must be a whole number.
____________________________________________________________
____________________________________________________________
Sorry, task 0 is not in your list yet.
____________________________________________________________
____________________________________________________________
Sorry, task -1 is not in your list yet.
____________________________________________________________
No tasks added yet.
Bye. Hope to see you again soon!
```

## Test: flexible parameter whitespace

**Aim:** Verify leading and trailing whitespace and tabs around parameters are accepted.

**Inputs:**
```text
  deadline   book	/by	2026-09-13  
  event meeting	/from	2pm	/to	4pm  
bye
```

**Expected output:**
```text
____________________________________________________________
Got it. I've added this task:
  [D][ ] book (by: Sep 13 2026)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] meeting (from: 2pm to: 4pm)
Now you have 2 tasks in the list.
____________________________________________________________
Bye. Hope to see you again soon!
```

## Test: end of input exits cleanly

**Aim:** Verify the console exits without an exception when its input ends without bye.

**Inputs:**
```text
list
```

**Expected output:**
```text
No tasks added yet.
Bye. Hope to see you again soon!
```

Additional manual GUI checks for A-MoreErrorHandling:

- Enter `q`: Bambolino shows the same goodbye response as `bye`.
- Enter `bye now`: an error card appears and the command remains available for correction.
- Enter an event with repeated `/from` or `/to`: an error card appears; `list` confirms no task was added.

The whitespace test intentionally includes trailing spaces and literal tabs in its input.
