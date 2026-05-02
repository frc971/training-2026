/*
 * EvaluationTest.java
 *
 * This file is for automated testing of your elevator implementation.
 *
 * DO NOT MODIFY THIS FILE.
 *
 * All of your code should pass the tests in this file without any changes to the test code.
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class EvaluationTest {
  // ANSI escape codes for colored output
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_RESET = "\u001B[0m";
  public static final boolean SUPPORTS_COLOR = supportsColor();

  public static void main(String[] args) {
    // Save original System.out
    PrintStream originalOut = System.out;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    Elevator elevator = new Elevator(1);
    ElevatorController controller = new ElevatorController(elevator, 1, 5);

    // Test initial state
    check(elevator.getCurrentFloor() == 1, "Initial floor should be 1");
    String expectedInit = "Elevator instantiated at floor 1\n";
    checkContains(normalize(outContent.toString()), expectedInit, "Missing instantiation output");

    // Test valid move up
    outContent.reset();
    controller.goToFloor(3);
    String expectedMoveUp =
        "Moving up... now at floor 2\nMoving up... now at floor 3\nArrived at floor 3\n";
    check(elevator.getCurrentFloor() == 3, "Should be at floor 3");
    checkEquals(
        normalize(outContent.toString()), expectedMoveUp, "Unexpected output for moving up");

    // Test valid move down
    outContent.reset();
    controller.goToFloor(2);
    String expectedMoveDown = "Moving down... now at floor 2\nArrived at floor 2\n";
    check(elevator.getCurrentFloor() == 2, "Should be at floor 2");
    checkEquals(
        normalize(outContent.toString()), expectedMoveDown, "Unexpected output for moving down");

    // Test invalid move (below min)
    outContent.reset();
    controller.goToFloor(0);
    String expectedInvalidLow = "Floor 0 is not a valid floor\n";
    check(elevator.getCurrentFloor() == 2, "Should remain at floor 2");
    checkEquals(
        normalize(outContent.toString()),
        expectedInvalidLow,
        "Unexpected output for invalid low floor");

    // Test invalid move (above max)
    outContent.reset();
    controller.goToFloor(6);
    String expectedInvalidHigh = "Floor 6 is not a valid floor\n";
    check(elevator.getCurrentFloor() == 2, "Should remain at floor 2");
    checkEquals(
        normalize(outContent.toString()),
        expectedInvalidHigh,
        "Unexpected output for invalid high floor");

    // Test move to max floor
    outContent.reset();
    controller.goToFloor(5);
    String expectedToMax =
        "Moving up... now at floor 3\n"
            + "Moving up... now at floor 4\n"
            + "Moving up... now at floor 5\n"
            + "Arrived at floor 5\n";
    check(elevator.getCurrentFloor() == 5, "Should be at floor 5");
    checkEquals(
        normalize(outContent.toString()),
        expectedToMax,
        "Unexpected output for moving to max floor");

    // Test move to min floor
    outContent.reset();
    controller.goToFloor(1);
    String expectedToMin =
        "Moving down... now at floor 4\n"
            + "Moving down... now at floor 3\n"
            + "Moving down... now at floor 2\n"
            + "Moving down... now at floor 1\n"
            + "Arrived at floor 1\n";
    check(elevator.getCurrentFloor() == 1, "Should be at floor 1");
    checkEquals(
        normalize(outContent.toString()),
        expectedToMin,
        "Unexpected output for moving to min floor");

    // Restore original System.out
    System.setOut(originalOut);
    printlnColor(ANSI_GREEN, "All tests passed.");
  }

  // Normalize line endings to \n
  private static String normalize(String text) {
    return text.replace("\r\n", "\n").replace("\r", "\n");
  }

  private static void check(boolean condition, String message) {
    if (!condition) {
      printlnColor(ANSI_RED, "TEST FAILED: " + message);
      System.exit(1);
    }
  }

  private static void checkEquals(String actual, String expected, String message) {
    if (!actual.equals(expected)) {
      printlnColor(ANSI_RED, "TEST FAILED: " + message);
      printlnColor(ANSI_RED, "Expected output:\n" + expected);
      printlnColor(ANSI_RED, "Actual output:\n" + actual);
      System.exit(1);
    }
  }

  private static void checkContains(String actual, String expectedSubstring, String message) {
    if (!actual.contains(expectedSubstring)) {
      printlnColor(ANSI_RED, "TEST FAILED: " + message);
      printlnColor(ANSI_RED, "Expected output to contain:\n" + expectedSubstring);
      printlnColor(ANSI_RED, "Actual output:\n" + actual);
      System.exit(1);
    }
  }

  private static void printlnColor(String color, String message) {
    if (SUPPORTS_COLOR) {
      if (color.equals(ANSI_RED)) {
        System.err.println(color + message + ANSI_RESET);
      } else {
        System.out.println(color + message + ANSI_RESET);
      }
    } else {
      if (color.equals(ANSI_RED)) {
        System.err.println(message);
      } else {
        System.out.println(message);
      }
    }
  }

  private static boolean supportsColor() {
    String os = System.getProperty("os.name").toLowerCase();
    String term = System.getenv("TERM");
    boolean isWindows = os.contains("win");
    boolean isXterm =
        term != null && (term.contains("xterm") || term.contains("ansi") || term.contains("color"));
    boolean hasConsole = System.console() != null;
    // Most modern terminals support color, but fallback for Windows/cmd or unknown
    return (!isWindows || isXterm) && hasConsole;
  }
}
