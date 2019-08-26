import java.util.Scanner;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class Duke {
    private static ArrayList<Task> dukeList = new ArrayList<>();
    private static DateTimeFormatter dukeDateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    public static void main(String[] args) {
        String initialMessage = "Hello! I'm Duke\nWhat can I do for you?";
        System.out.println(initialMessage);

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] inputSplit = input.split(" ");
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                return;
            } else if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                displayDukeList();
            } else if (inputSplit[0].equals("done")) {
                int inputIndex = Integer.parseInt(inputSplit[1]);
                int actualListIndex = inputIndex - 1;
                Task targetTask = dukeList.get(actualListIndex);
                targetTask.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(targetTask);
            } else if (inputSplit[0].equals("todo")) {
                try {
                    handleInputTodo(input);
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
            } else if (inputSplit[0].equals("deadline")) {
                try {
                    handleInputDeadline(input);
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
            } else if (inputSplit[0].equals("event")) {
                try {
                    handleInputEvent(input);
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
            } else if (inputSplit[0].equals("delete")) {
                int inputIndex = Integer.parseInt(inputSplit[1]);
                int actualIndex = inputIndex - 1;
                Task toBeRemoved = dukeList.get(actualIndex);
                System.out.println("Noted. I've removed this task:");
                System.out.println(toBeRemoved);
                dukeList.remove(actualIndex);
                System.out.println("Now you have " + dukeList.size() + " tasks in the list.");
            } else {
                try {
                    handleInputUnrecognised(input);
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void displayDukeList() {
        for (int i = 0; i < dukeList.size(); i++) {
            int itemIndex = i + 1;
            String itemDisplay = itemIndex + "." + dukeList.get(i).toString();
            System.out.println(itemDisplay);
        }
    }

    public static int slashLocator(String sentence) {
        return sentence.indexOf("/");
    }

    public static void handleInputTodo(String inputTodo) throws DukeException {
        if (inputTodo.length() == 4) {
            throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
        } else {
            String todoDescription = inputTodo.substring(5);
            Todo t = new Todo(todoDescription);
            dukeList.add(t);
            System.out.println("Got it. I've added this task:");
            System.out.println(t);
            System.out.println("Now you have " + dukeList.size() + " tasks in the list.");
        }
    }

    public static void handleInputDeadline(String inputDeadline) throws DukeException {
        if (inputDeadline.length() == 8) {
            throw new DukeException("☹ OOPS!!! The description of a deadline cannot be empty.");
        } else {
            int slashLocation = slashLocator(inputDeadline);
            int firstIndex = slashLocation - 1;
            int secondIndex = slashLocation + 4;
            String deadlineDescription = inputDeadline.substring(9, firstIndex);
            String deadlineBy = inputDeadline.substring(secondIndex);
            Deadline d = new Deadline(deadlineDescription, deadlineBy);

            addTaskToListAfterValidation(deadlineBy, d);

        }
    }

    public static void handleInputEvent(String inputEvent) throws DukeException {
        if (inputEvent.length() == 5) {
            throw new DukeException("☹ OOPS!!! The description of a event cannot be empty.");
        } else {
            int slashLocation = slashLocator(inputEvent);
            int firstIndex = slashLocation - 1;
            int secondIndex = slashLocation + 4;
            String eventDescription = inputEvent.substring(6, firstIndex);
            String eventAt = inputEvent.substring(secondIndex);
            Event e = new Event(eventDescription, eventAt);

            addTaskToListAfterValidation(eventAt, e);

        }
    }

    public static void handleInputUnrecognised(String inputUnrecognised) throws DukeException {
        throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    public static void addTaskToListAfterValidation(String dateTimeString, Task t) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, dukeDateTimeFormatter);

            if (t instanceof Event) {
                Event e = (Event) t;
                e.setDateTimeAt(dateTime);
            } else {
                Deadline d = (Deadline) t;
                d.setDateTimeBy(dateTime);
            }

            dukeList.add(t);
            System.out.println("Got it. I've added this task:");
            System.out.println(t);
            System.out.println("Now you have " + dukeList.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("Task not added to list because the input format for date and time is unrecognised. " +
                    "Please enter date and time in dd/MM/yyyy HHmm format.");
        }
    }

}
