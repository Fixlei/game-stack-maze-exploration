package edu.pdpStack;

/**
 * Represents a position (row, col) in the maze grid.
 */
public class Position {
  public final int row;
  public final int col;

  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Position)) {
      return false;
    }
    Position p = (Position) o;
    return row == p.row && col == p.col;
  }

  @Override
  public int hashCode() {
    return 31 * row + col;
  }

  @Override
  public String toString() {
    return "(" + row + ", " + col + ")";
  }
}