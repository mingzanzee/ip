import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Runs the console UI cases described in {@code src/test/ui-test-plan.md}.
 *
 * <p>All cases run in one test method so a failed case immediately ends the
 * test session instead of allowing later UI cases to run.</p>
 */
class UiTest {
    /** The project-relative data file used by the application and checked by the save test. */
    private static final Path SAVE_FILE = Path.of("data", "tardt.txt");

    /** Runs every planned UI case and prints its input and captured output. */
    @Test
    void runsUiTestPlan() {
        String welcome = "Hello! I'm Tard_T. \n"
                + "What can I do for you? \n"
                + "________________________________\n\n";
        String separator = "________________________________\n";
        String goodbye = separator + "Bye. Hope to see you again soon!\n" + separator;

        List<UiTestCase> cases = List.of(
                new UiTestCase(
                        "Exit the application",
                        "Verify that the application shows its welcome message and exits politely when the user enters bye.",
                        "bye\n",
                        welcome + goodbye,
                        null),
                new UiTestCase(
                        "Save a newly added task",
                        "Verify that a successful task addition writes the current task list to data/duke.txt.",
                        "todo read book\nbye\n",
                        welcome + separator
                                + "Got it. I've added this task:\n"
                                + "  [T][ ] read book\n"
                                + "Now you have 1 tasks in the list.\n"
                                + separator + goodbye,
                        "T | 0 | read book"),
                new UiTestCase(
                        "Load saved tasks at startup",
                        "Verify that tasks from data/tardt.txt are reconstructed and listed when the application starts.",
                        "list\nbye\n",
                        welcome + separator
                                + "1. [T][X] read book\n"
                                + "2. [D][ ] return book (by: Aug 23 2026 15:00)\n"
                                + "3. [E][ ] project meeting (from: Oct 20 2026 15:00 to: Oct 20 2026 17:00)\n"
                                + separator + goodbye,
                        "T | 1 | read book\n"
                                + "D | 0 | return book | 2026-08-23T15:00\n"
                                + "E | 0 | project meeting | 2026-10-20T15:00 | 2026-10-20T17:00",
                        null),
                new UiTestCase(
                        "Reject an unknown command without changing tasks",
                        "Verify that an invalid command is rejected and that the following valid command creates the first task.",
                        "nonsense\ntodo read book\nlist\nbye\n",
                        welcome + separator
                                + "'nonsense' is not a valid input.\n"
                                + "Valid input formats: \n"
                                + "bye -> exits the interface\n"
                                + "list -> lists all the tasks and their status\n"
                                + "mark [task number] -> marks the task and show their status\n"
                                + "unmark [task number] -> unmarks the task and show their status\n"
                                + "todo [task name] -> adds a todo task to taskList\n"
                                + "deadline [task name] /by [deadline] -> adds a deadline task to taskList\n"
                                + "event [task name] /from [start time] /to [end time] -> adds an event task to taskList\n"
                                + "delete [task number] -> deletes a task from taskList\n"
                                + separator + separator
                                + "Got it. I've added this task:\n"
                                + "  [T][ ] read book\n"
                                + "Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "1. [T][ ] read book\n"
                                + separator + goodbye,
                        null),
                new UiTestCase(
                        "Add and list a deadline",
                        "Verify that a valid deadline is stored and displayed with its deadline value.",
                        "deadline submit assignment /by 2026-10-20T17:00\nlist\nbye\n",
                        welcome + separator
                                + "    Got it. I've added this task:\n"
                                + "      [D][ ] submit assignment (by: Oct 20 2026 17:00)\n"
                                + "    Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "1. [D][ ] submit assignment (by: Oct 20 2026 17:00)\n"
                                + separator + goodbye,
                        null),
                new UiTestCase(
                        "Reject a malformed deadline without changing tasks",
                        "Verify that a malformed deadline is rejected and does not add a second task.",
                        "todo read book\ndeadline submit assignment\nlist\nbye\n",
                        welcome + separator
                                + "Got it. I've added this task:\n"
                                + "  [T][ ] read book\n"
                                + "Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "    Invalid format. Use: deadline [task name] /by [deadline]\n"
                                + separator + separator
                                + "1. [T][ ] read book\n"
                                + separator + goodbye,
                        null),
                new UiTestCase(
                        "Add and list an event",
                        "Verify that a valid event is stored and displayed with its start and end times.",
                        "event lecture /from 2026-10-20T15:00 /to 2026-10-20T16:00\nlist\nbye\n",
                        welcome + separator
                                + "    Got it. I've added this task:\n"
                                + "      [E][ ] lecture (from: Oct 20 2026 15:00 to: Oct 20 2026 16:00)\n"
                                + "    Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "1. [E][ ] lecture (from: Oct 20 2026 15:00 to: Oct 20 2026 16:00)\n"
                                + separator + goodbye,
                        null),
                new UiTestCase(
                        "Reject a non-numeric mark without changing task status",
                        "Verify that an invalid mark number is rejected and the existing task remains unmarked.",
                        "todo read book\nmark one\nlist\nbye\n",
                        welcome + separator
                                + "Got it. I've added this task:\n"
                                + "  [T][ ] read book\n"
                                + "Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "    'one' is not a valid integer.\n"
                                + separator + separator + separator
                                + "1. [T][ ] read book\n"
                                + separator + goodbye,
                        null),
                new UiTestCase(
                        "Delete a task and list the remaining task",
                        "Verify that delete removes the specified task and list renumbers the remaining task.",
                        "todo read book\ndeadline submit assignment /by 2026-10-20T17:00\ndelete 1\nlist\nbye\n",
                        welcome + separator
                                + "Got it. I've added this task:\n"
                                + "  [T][ ] read book\n"
                                + "Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "    Got it. I've added this task:\n"
                                + "      [D][ ] submit assignment (by: Oct 20 2026 17:00)\n"
                                + "    Now you have 2 tasks in the list.\n"
                                + separator + separator
                                + "Noted, I've removed this task: \n"
                                + "  [T][ ] read book\n"
                                + "    Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "1. [D][ ] submit assignment (by: Oct 20 2026 17:00)\n"
                                + separator + goodbye,
                        null),
                new UiTestCase(
                        "Reject a non-numeric delete without changing tasks",
                        "Verify that an invalid delete number is rejected and the existing task remains in the list.",
                        "todo read book\ndelete one\nlist\nbye\n",
                        welcome + separator
                                + "Got it. I've added this task:\n"
                                + "  [T][ ] read book\n"
                                + "Now you have 1 tasks in the list.\n"
                                + separator + separator
                                + "    'one' is not a valid integer.\n"
                                + separator + separator
                                + "1. [T][ ] read book\n"
                                + separator + goodbye,
                        null)
        );

        boolean saveFileExisted = false;
        String originalSavedData = null;
        try {
            saveFileExisted = Files.exists(SAVE_FILE);
            if (saveFileExisted) {
                originalSavedData = Files.readString(SAVE_FILE);
            }
            for (UiTestCase testCase : cases) {
                deleteSaveFile();
                writeInitialSavedData(testCase.initialSavedData());
                String actualOutput = runApplication(testCase.input());
                printSessionRecord(testCase, actualOutput);
                assertEquals(testCase.expectedOutput(), actualOutput,
                        () -> "UI test failed: " + testCase.name()
                                + "\nAim: " + testCase.aim()
                                + "\nConsole input:\n" + testCase.input()
                                + "\nExpected output:\n" + testCase.expectedOutput()
                                + "\nActual output:\n" + actualOutput);
                if (testCase.expectedSavedData() != null) {
                    assertEquals(testCase.expectedSavedData(), Files.readString(SAVE_FILE),
                            () -> "Saved task data did not match after: " + testCase.name());
                }
            }
        } catch (IOException exception) {
            throw new AssertionError("Unable to inspect the test save file.", exception);
        } finally {
            restoreSaveFile(saveFileExisted, originalSavedData);
        }
    }

    /** Runs the application once with the supplied console input and captures its output. */
    private String runApplication(String input) {
        PrintStream originalOutput = System.out;
        java.io.InputStream originalInput = System.in;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));
            Tard_T.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
        return capturedOutput.toString(StandardCharsets.UTF_8);
    }

    /** Prints a readable record of one completed console test session. */
    private void printSessionRecord(UiTestCase testCase, String actualOutput) {
        System.out.printf("""
                UI test case: %s
                Aim: %s
                Console input:
                %sConsole output:
                %s""", testCase.name(), testCase.aim(), testCase.input(), actualOutput);
    }

    /** Deletes the save file so each UI case starts with an isolated file state. */
    private void deleteSaveFile() {
        try {
            Files.deleteIfExists(SAVE_FILE);
        } catch (IOException exception) {
            throw new AssertionError("Unable to reset the test save file.", exception);
        }
    }

    /** Writes the test case's initial data so the application can load it at startup. */
    private void writeInitialSavedData(String initialSavedData) throws IOException {
        if (initialSavedData == null) {
            return;
        }
        Files.createDirectories(SAVE_FILE.getParent());
        Files.writeString(SAVE_FILE, initialSavedData);
    }

    /** Restores any data that existed before the UI test suite ran. */
    private void restoreSaveFile(boolean saveFileExisted, String originalSavedData) {
        deleteSaveFile();
        if (!saveFileExisted) {
            return;
        }
        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.writeString(SAVE_FILE, originalSavedData);
        } catch (IOException exception) {
            throw new AssertionError("Unable to restore the original save file.", exception);
        }
    }

    /** Stores all information needed to run one planned console UI test. */
    private record UiTestCase(String name, String aim, String input, String expectedOutput,
                              String initialSavedData, String expectedSavedData) {
        /** Creates a case without pre-existing saved data. */
        UiTestCase(String name, String aim, String input, String expectedOutput,
                   String expectedSavedData) {
            this(name, aim, input, expectedOutput, null, expectedSavedData);
        }
    }
}
