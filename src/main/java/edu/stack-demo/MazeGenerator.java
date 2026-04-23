package edu.pdpStack;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

/**
 * Maze Generator — uses Stack + DFS to create random mazes.
 *
 * Algorithm: Randomized DFS (Recursive Backtracker with explicit stack)
 *  1. Start from initial cell, push onto stack
 *  2. Randomly pick an unvisited neighbor, carve the wall, push onto stack
 *  3. If no unvisited neighbors, pop from stack (backtrack)
 *  4. Repeat until stack is empty — maze is complete
 *
 * Cell encoding:
 *  0 = open path
 *  1 = wall
 *  2 = start point
 *  3 = end point
 */
public class MazeGenerator {

  private static final int WALL = 1;
  private static final int PATH = 0;

  private final int rows, cols;
  private final Random rand;

  public MazeGenerator(int rows, int cols) {
    // Ensure odd dimensions for proper maze structure
    this.rows = rows % 2 == 0 ? rows + 1 : rows;
    this.cols = cols % 2 == 0 ? cols + 1 : cols;
    this.rand = new Random();
  }

  /**
   * Generate a maze and return as a 2D array.
   */
  public int[][] generate() {
    int[][] maze = new int[rows][cols];
    // Initialize all cells as walls
    for (int[] row : maze) Arrays.fill(row, WALL);

    // ===== CORE: Stack-based DFS generation =====
    GameStack<Position> stack = new GameStack<>();
    boolean[][] visited = new boolean[rows][cols];

    // Start from (1, 1)
    Position start = new Position(1, 1);
    maze[1][1] = PATH;
    visited[1][1] = true;
    stack.push(start);

    // Four directions: up, down, left, right (step size 2, carve wall in between)
    int[][] dirs = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};

    while (!stack.isEmpty()) {
      Position cur = stack.peek();
      List<int[]> shuffled = Arrays.asList(dirs.clone());
      Collections.shuffle(shuffled, rand);

      boolean found = false;
      for (int[] d : shuffled) {
        int nr = cur.row + d[0], nc = cur.col + d[1];
        if (inBounds(nr, nc) && !visited[nr][nc]) {
          // Carve the wall between current cell and neighbor
          maze[cur.row + d[0] / 2][cur.col + d[1] / 2] = PATH;
          maze[nr][nc] = PATH;
          visited[nr][nc] = true;
          stack.push(new Position(nr, nc));
          found = true;
          break;
        }
      }

      if (!found) {
        stack.pop(); // STACK BACKTRACK: no unvisited neighbors, pop
      }
    }

    // Set start and end points
    maze[1][1] = 2;                         // start
    maze[rows - 2][cols - 2] = 3;           // end

    return maze;
  }

  private boolean inBounds(int r, int c) {
    return r > 0 && r < rows - 1 && c > 0 && c < cols - 1;
  }

  public int getRows() { return rows; }
  public int getCols() { return cols; }
}