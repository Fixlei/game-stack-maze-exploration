package edu.pdpStack;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Console Maze Renderer — displays the maze with ANSI color codes.
 */
public class MazeRenderer {

  // ANSI color codes
  private static final String RESET  = "\033[0m";
  private static final String RED    = "\033[91m";
  private static final String GREEN  = "\033[92m";
  private static final String YELLOW = "\033[93m";
  private static final String BLUE   = "\033[94m";
  private static final String CYAN   = "\033[96m";
  private static final String GRAY   = "\033[90m";
  private static final String BG_GREEN = "\033[42m";
  private static final String BG_RED   = "\033[41m";
  private static final String WHITE  = "\033[97m";
  private static final String BOLD   = "\033[1m";

  /**
   * Render the full game screen.
   */
  public void render(int[][] maze, Position player, GameStack<Position> undoStack,
                     int steps, List<Position> solvedPath) {
    clearScreen();

    int rows = maze.length, cols = maze[0].length;

    // Build path set for highlighting
    Set<Position> pathSet = new HashSet<>();
    if (solvedPath != null) {
      pathSet.addAll(solvedPath);
    }

    // Build trail set from undo history (player's footprints)
    Set<Position> trail = new HashSet<>();
    if (undoStack != null) {
      for (Object o : undoStack.toArray()) trail.add((Position) o);
    }

    // === Title ===
    System.out.println(BOLD + CYAN + "+-----------------------------------------+" + RESET);
    System.out.println(BOLD + CYAN + "|      Stack Maze Explorer                |" + RESET);
    System.out.println(BOLD + CYAN + "+-----------------------------------------+" + RESET);
    System.out.println();

    // === Maze body ===
    for (int r = 0; r < rows; r++) {
      StringBuilder line = new StringBuilder("  ");
      for (int c = 0; c < cols; c++) {
        Position pos = new Position(r, c);

        if (player != null && player.equals(pos)) {
          line.append(BOLD + YELLOW + "@ " + RESET);  // player
        } else if (maze[r][c] == 2) {
          line.append(BG_GREEN + WHITE + "S " + RESET); // start
        } else if (maze[r][c] == 3) {
          line.append(BG_RED + WHITE + "E " + RESET);   // end
        } else if (pathSet.contains(pos)) {
          line.append(GREEN + "* " + RESET);  // auto-solve path
        } else if (trail.contains(pos)) {
          line.append(BLUE + ". " + RESET);   // player trail
        } else if (maze[r][c] == 1) {
          line.append(GRAY + "##" + RESET);   // wall
        } else {
          line.append("  ");                   // open path
        }
      }
      System.out.println(line);
    }

    // === Status info ===
    System.out.println();
    System.out.println(CYAN + "  Steps: " + RESET + steps
        + CYAN + "  |  Undo stack size: " + RESET
        + (undoStack != null ? undoStack.size() : 0));

    if (undoStack != null && !undoStack.isEmpty()) {
      System.out.println(GRAY + "  " + undoStack.visualize(6) + RESET);
    }

    System.out.println();
    System.out.println(GRAY + "  W=Up  S=Down  A=Left  D=Right  U=Undo  R=Reset  P=Solve  Q=Quit" + RESET);
    System.out.print(YELLOW + "  > " + RESET);
  }

  /**
   * Render a single step during auto-solve animation.
   */
  public void renderSolveStep(int[][] maze, Position current,
                              GameStack<Position> stack, int step, boolean backtrack) {
    clearScreen();
    int rows = maze.length, cols = maze[0].length;

    Set<Position> trail = new HashSet<>();
    for (Object o : stack.toArray()) trail.add((Position) o);

    System.out.println(BOLD + CYAN + "  Auto-solving... Step " + step
        + (backtrack ? RED + " [BACKTRACK]" : GREEN + " [FORWARD]") + RESET);
    System.out.println();

    for (int r = 0; r < rows; r++) {
      StringBuilder line = new StringBuilder("  ");
      for (int c = 0; c < cols; c++) {
        Position pos = new Position(r, c);
        if (pos.equals(current)) {
          line.append(BOLD + YELLOW + "@ " + RESET);
        } else if (maze[r][c] == 2) {
          line.append(BG_GREEN + WHITE + "S " + RESET);
        } else if (maze[r][c] == 3) {
          line.append(BG_RED + WHITE + "E " + RESET);
        } else if (trail.contains(pos)) {
          line.append(GREEN + "* " + RESET);
        } else if (maze[r][c] == 1) {
          line.append(GRAY + "##" + RESET);
        } else {
          line.append("  ");
        }
      }
      System.out.println(line);
    }

    System.out.println();
    System.out.println(CYAN + "  " + stack.visualize(8) + RESET);
  }

  /** Display the victory screen. */
  public void renderWin(int steps) {
    System.out.println();
    System.out.println(BOLD + GREEN + "  +---------------------------+" + RESET);
    System.out.println(BOLD + GREEN + "  |  You escaped the maze!    |" + RESET);
    System.out.println(BOLD + GREEN + "  |  Total steps: " + String.format("%-11d", steps) + " |" + RESET);
    System.out.println(BOLD + GREEN + "  +---------------------------+" + RESET);
    System.out.println();
  }

  /** Clear the terminal screen. */
  private void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
}