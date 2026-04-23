package edu.pdpStack;
import java.util.List;
import java.util.Scanner;

/**
 * ===================================================
 *   Stack Maze Explorer — Classic Stack Applications
 * ===================================================
 *
 * Stack usage in this project:
 *   1. GameStack      — Custom stack data structure (no java.util.Stack)
 *   2. MazeGenerator  — DFS maze generation using explicit stack
 *   3. MazeSolver     — DFS maze solving with stack-based backtracking
 *   4. Undo (this class) — Move history stored on a stack
 *
 * Build & Run:
 *   javac -d out src/*.java && java -cp out MazeGame
 */
public class MazeGame {

  private static final int MAZE_ROWS = 15;
  private static final int MAZE_COLS = 21;
  private static final int WALL = 1;

  private int[][] maze;
  private Position player;
  private Position start;
  private Position end;
  private int steps;
  private boolean solved;

  // ===== STACK APPLICATION: Undo history =====
  private GameStack<Position> undoStack;

  private MazeGenerator generator;
  private MazeSolver solver;
  private MazeRenderer renderer;

  public MazeGame() {
    generator = new MazeGenerator(MAZE_ROWS, MAZE_COLS);
    solver = new MazeSolver();
    renderer = new MazeRenderer();
    undoStack = new GameStack<>();
    initNewMaze();
  }

  /** Initialize a new random maze. */
  private void initNewMaze() {
    maze = generator.generate();
    start = new Position(1, 1);
    end = new Position(generator.getRows() - 2, generator.getCols() - 2);
    player = new Position(start.row, start.col);
    steps = 0;
    solved = false;
    undoStack.clear();
  }

  /** Main game loop. */
  public void run() {
    Scanner sc = new Scanner(System.in);

    while (true) {
      renderer.render(maze, player, undoStack, steps, null);

      if (!sc.hasNextLine()) break;
      String input = sc.nextLine().trim().toLowerCase();
      if (input.isEmpty()) continue;

      char cmd = input.charAt(0);

      switch (cmd) {
        case 'w': move(-1, 0); break;  // up
        case 's': move(1, 0); break;   // down
        case 'a': move(0, -1); break;  // left
        case 'd': move(0, 1); break;   // right
        case 'u': undo(); break;        // STACK: undo
        case 'p': autoSolve(); break;   // STACK: DFS solve
        case 'r': initNewMaze(); break; // new maze
        case 'q':
          System.out.println("  Goodbye!");
          return;
        default:
          break;
      }

      // Check if player reached the exit
      if (player.equals(end)) {
        renderer.render(maze, player, undoStack, steps, null);
        renderer.renderWin(steps);
        System.out.print("  Press R to play again, Q to quit: ");
        if (sc.hasNextLine()) {
          String choice = sc.nextLine().trim().toLowerCase();
          if ("r".equals(choice)) {
            initNewMaze();
          } else {
            return;
          }
        }
      }
    }

    sc.close();
  }

  /** Move the player by (dr, dc). */
  private void move(int dr, int dc) {
    int nr = player.row + dr, nc = player.col + dc;
    if (nr >= 0 && nr < maze.length && nc >= 0 && nc < maze[0].length
        && maze[nr][nc] != WALL) {

      // STACK APPLICATION: push current position for undo
      undoStack.push(new Position(player.row, player.col));

      player = new Position(nr, nc);
      steps++;
    }
  }

  /**
   * Undo the last move — classic stack application.
   * Pop the previous position from undoStack to restore player location.
   */
  private void undo() {
    if (undoStack.isEmpty()) {
      return; // nothing to undo
    }
    player = undoStack.pop(); // STACK: pop to restore
    steps++;
  }

  /**
   * Auto-solve using stack-based DFS.
   * Displays the stack changing in real time.
   */
  private void autoSolve() {
    List<Position> path = solver.solve(maze, start, end,
        new MazeSolver.SolveListener() {
          @Override
          public void onStep(Position current, GameStack<Position> stack, int stepCount) {
            renderer.renderSolveStep(maze, current, stack, stepCount, false);
            sleep(80); // animation delay
          }

          @Override
          public void onBacktrack(Position from, GameStack<Position> stack) {
            renderer.renderSolveStep(maze, from, stack, -1, true);
            sleep(40);
          }
        });

    if (!path.isEmpty()) {
      renderer.render(maze, end, undoStack, path.size(), path);
      System.out.println("  Solved! Path length: " + path.size());
      solved = true;
    } else {
      System.out.println("  No solution exists!");
    }
  }

  private void sleep(int ms) {
    try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
  }

  // ==================== Entry Point ====================
  public static void main(String[] args) {
    System.out.println("\033[H\033[2J"); // clear screen
    System.out.println();
    System.out.println("  +---------------------------------------+");
    System.out.println("  |   Stack Maze Explorer                 |");
    System.out.println("  |                                       |");
    System.out.println("  |   Stack applications demonstrated:    |");
    System.out.println("  |   * DFS maze generation (stack)       |");
    System.out.println("  |   * DFS maze solving   (stack)        |");
    System.out.println("  |   * Undo operation     (stack)        |");
    System.out.println("  +---------------------------------------+");
    System.out.println();
    System.out.println("  Press Enter to start...");

    try { System.in.read(); } catch (Exception ignored) {}

    new MazeGame().run();
  }
}