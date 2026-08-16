import java.util.Scanner;

public class Bambolino {
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
        String[] tasks = new String[100];
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
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + ". " + tasks[i]);
                    }
                }
            }else{
                System.out.println("added:" + userInput);
                tasks[taskCount] = userInput;
                taskCount++;
            }

        }
    }
}
