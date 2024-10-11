import task.Deadline;
import task.Event;
import task.Task;
import task.ToDo;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Flash {

    private static List<Task> tasks = new ArrayList<>();
    private static final String FILE_PATH = "./data/flash.txt";
    private static Storage storage;

    public static void displayTasks() {
        System.out.println("____________________________________________________________");
        for(int i = 0; i < tasks.size(); i++) {
            System.out.println(i+1 + "." + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }

    public static void markTask(String input) throws FlashException {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = tasks.get(taskNumber);
            task.markDone();
            System.out.println("____________________________________________________________");
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(" " + task);
            System.out.println("____________________________________________________________");
        } catch (Exception e) {
            throw new FlashException("Invalid task number. Please enter a valid task number.");
        }
    }

    public static void unMarkTask(String input) throws FlashException {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = tasks.get(taskNumber);
            task.markNotDone();
            System.out.println("____________________________________________________________");
            System.out.println("Ok, I've marked this task as not done yet:");
            System.out.println(" " + task);
            System.out.println("____________________________________________________________");
        } catch (Exception e) {
            throw new FlashException("Invalid task number. Please enter a valid task number.");
        }
    }

    public static void todo(String input) throws FlashException {
        if (input.length() <= 5) {
            throw new FlashException("Uh-oh! Description for Todo Needed!! Cannot be left empty.");
        }

        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new FlashException("Uh-oh! Description for task.ToDo Needed!! Cannot be left empty.");
        }

        Task task = new ToDo(description);
        tasks.add(task);
        System.out.println("____________________________________________________________");
        System.out.println(" " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public static void deadline(String input) throws FlashException {
        try {
            String[] parts = input.replaceFirst("deadline ", "").split(" /by ");
            String description = parts[0].trim();
            String by = parts[1].trim();
            Task task = new Deadline(description, by);
            tasks.add(task);
            System.out.println("____________________________________________________________");
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + task);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FlashException("Uh-oh! Description for task.Event Needed!! Cannot be left empty.");
        }
    }

    public static void event(String input) throws FlashException {
        try {
            String[] parts = input.replaceFirst("event ", "").split(" /from | /to ");
            String description = parts[0].trim();
            String from = parts[1].trim();
            String to = parts[2].trim();
            Task task = new Event(description, from, to);
            tasks.add(task);
            System.out.println("____________________________________________________________");
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + task);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FlashException("Uh-oh! Description for task.Event Needed!! Cannot be left empty.");
        }
    }

    public static void deleteTask(String input) throws FlashException {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = tasks.get(taskNumber);
            tasks.remove(taskNumber);

            System.out.println("____________________________________________________________");
            System.out.println("Noted. I've removed this task:");
            System.out.println(" " + task);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new FlashException("Uh-oh! task.Task number is needed for deletion. Enter a valid task number.");
        } catch (IndexOutOfBoundsException e) {
            throw new FlashException("Invalid task number. Please enter a valid task number.");
        }
    }


    public static void main(String[] args) {
        storage = new Storage(FILE_PATH);

        // Load tasks from file
        try {
            tasks = storage.load();
            System.out.println("Loaded tasks from file successfully.");
        } catch (FlashException e) {
            System.out.println("Failed to load tasks: " + e.getMessage());
        }

        Scanner in = new Scanner(System.in);
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Flash");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        while(true) {
            try {
                String input = in.nextLine();
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println("____________________________________________________________");
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    displayTasks();
                } else if (input.startsWith("mark")) {
                    markTask(input);
                    storage.save(tasks);
                } else if (input.startsWith("unmark")) {
                    unMarkTask(input);
                    storage.save(tasks);
                } else if (input.startsWith("todo")) {
                    todo(input);
                    storage.save(tasks);
                } else if (input.startsWith("deadline")) {
                    deadline(input);
                    storage.save(tasks);
                } else if (input.startsWith("event")) {
                    event(input);
                } else if (input.startsWith("delete")) {
                    deleteTask(input);
                    storage.save(tasks);
                } else {
                    throw new FlashException("Uh-oh! I don't know what that means.");
                }
            } catch (FlashException e){
                System.out.println("____________________________________________________________");
                System.out.println(e.getMessage());
                System.out.println("____________________________________________________________");
            }
        }
    }
}
