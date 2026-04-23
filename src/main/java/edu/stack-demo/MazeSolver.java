package edu.pdpStack;
import java.util.ArrayList;
import java.util.List;

/**
 * Maze Solver — uses Stack + DFS to find a path from start to end.
 *
 * Algorithm:
 *  1. Push start position onto stack
 *  2. Peek at stack top (current position)
 *  3. If it's the end → return stack contents as the solution path
 *  4. Find an unvisited walkable neighbor → push onto stack
 *  5. No available neighbors → pop stack top (backtrack)
 *  6. Stack empty → no solution exists
 */
public class MazeSolver {

  private static final int WALL = 1;
  private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

  /**
   * Solve the maze and return the path as a list of positions.
   * Returns an empty list if no solution exists.
   *
   * @param maze     the maze grid
   * @param start    start position
   * @param end      end position
   * @param listener callback for each step (used for animation)
   */
  public List<Position> solve(int[][] maze, Position start, Position end,
                              SolveListener listener) {
    int rows = maze.length, cols = maze[0].length;
    boolean[][] visited = new boolean[rows][cols];

    // ===== CORE: Stack-based DFS =====
    GameStack<Position> stack = new GameStack<>();
    stack.push(start);
    visited[start.row][start.col] = true;

    int stepCount = 0;

    while (!stack.isEmpty()) {
      Position cur = stack.peek();
      stepCount++;

      // Notify listener (for real-time stack visualization)
      if (listener != null) {
        listener.onStep(cur, stack, stepCount);
      }

      // Reached the end
      if (cur.equals(end)) {
        return stackToPath(stack);
      }

      // Look for an unvisited neighbor
      boolean found = false;
      for (int[] d : DIRS) {
        int nr = cur.row + d[0], nc = cur.col + d[1];
        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
            && !visited[nr][nc] && maze[nr][nc] != WALL) {
          visited[nr][nc] = true;
          stack.push(new Position(nr, nc));
          found = true;
          break;
        }
      }

      if (!found) {
        if (listener != null) {
          listener.onBacktrack(cur, stack);
        }
      }
    }

    return new ArrayList<>(); // No solution
  }

  /** Convert stack contents to a path list. */
  private List<Position> stackToPath(GameStack<Position> stack) {
    Object[] arr = stack.toArray();
    List<Position> path = new ArrayList<>();
    for (Object o : arr) path.add((Position) o);
    return path;
  }

  /** Callback interface for solve process visualization. */
  public interface SolveListener {
    void onStep(Position current, GameStack<Position> stack, int stepCount);
    void onBacktrack(Position from, GameStack<Position> stack);
  }
}