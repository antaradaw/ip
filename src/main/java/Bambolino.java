import java.util.Scanner;

/**
 * A command-line task list application.
 */
public class Bambolino {
    /** The line used to separate Bambolino's responses. */
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Starts Bambolino and processes commands until the user says goodbye.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String banner = " ____                  _           _ _             \n"
                + "| __ )  __ _ _ __ ___ | |__   ___ | (_)_ __   ___  \n"
                + "|  _ \\ / _` | '_ ` _ \\| '_ \\ / _ \\| | | '_ \\ / _ \\ \n"
                + "| |_) | (_| | | | | | | |_) | (_) | | | | | | (_) |\n"
                + "|____/ \\__,_|_| |_| |_|_.__/ \\___/|_|_|_| |_|\\___/ \n";
        System.out.println(banner);
        System.out.println("Hello! I'm Bambolino.\n" +
                "What can I do for you?");
        String userInput;
        Task[] tasks = new Task[100];
        int taskCount = 0;
        while (true){
            userInput = sc.nextLine();

            if (userInput.equalsIgnoreCase("Bye")){
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (userInput.equalsIgnoreCase("List")) {
                if (taskCount == 0){
                    System.out.println("No tasks added yet.");
                } else {
                    System.out.println(DIVIDER);
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                    System.out.println(DIVIDER);
                }
            } else if (userInput.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(userInput.substring(5));
                Task task = tasks[taskNumber - 1];
                task.markAsDone();
                System.out.println(DIVIDER);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
                System.out.println(DIVIDER);
            } else if (userInput.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(userInput.substring(7));
                Task task = tasks[taskNumber - 1];
                task.unmarkAsDone();
                System.out.println(DIVIDER);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
                System.out.println(DIVIDER);
            }else{
                System.out.println("added:" + userInput);
                tasks[taskCount] = new Task(userInput);
                taskCount++;
            }

        }
    }
}
