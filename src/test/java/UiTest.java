import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

    /** Runs every planned UI case and prints its input and captured output. */
    @Test
    void runsUiTestPlan() {
        List<UiTestCase> cases = List.of(
                new UiTestCase(
                        "Exit the application",
                        "Verify that the application shows its welcome message and exits politely when the user enters bye.",
                        "bye\n",
                        "Hello! I'm Tard_T. \n"
                                + "What can I do for you? \n"
                                + "________________________________\n\n"
                                + "________________________________\n"
                                + "Bye. Hope to see you again soon!\n"
                                + "________________________________\n"),
                new UiTestCase(
                        "Add and list a to-do task",
                        "Verify that a todo command adds a task and that list displays the task with its to-do status.",
                        "todo read book\nlist\nbye\n",
                        "Hello! I'm Tard_T. \n"
                                + "What can I do for you? \n"
                                + "________________________________\n\n"
                                + "________________________________\n"
                                + "Got it. I've added this task:\n"
                                + "  [T][ ] read book\n"
                                + "Now you have 1 tasks in the list.\n"
                                + "________________________________\n"
                                + "________________________________\n"
                                + "1. [T][ ] read book\n"
                                + "________________________________\n"
                                + "________________________________\n"
                                + "Bye. Hope to see you again soon!\n"
                                + "________________________________\n")
        );

        for (UiTestCase testCase : cases) {
            String actualOutput = runApplication(testCase.input());
            printSessionRecord(testCase, actualOutput);
            assertEquals(testCase.expectedOutput(), actualOutput,
                    () -> "UI test failed: " + testCase.name()
                            + "\nAim: " + testCase.aim()
                            + "\nConsole input:\n" + testCase.input()
                            + "\nExpected output:\n" + testCase.expectedOutput()
                            + "\nActual output:\n" + actualOutput);
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

    /** Stores all information needed to run one planned console UI test. */
    private record UiTestCase(String name, String aim, String input, String expectedOutput) {
    }
}
