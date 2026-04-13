package edu.pdpStack;

/**
 * Custom generic Stack implementation — no java.util.Stack dependency.
 * Built on a dynamic array to demonstrate core stack principles.
 * <p>
 * Used in this project for:
 * 1. DFS maze solving   — stores the search path.
 * 2. DFS maze generation — stack-based recursive backtracking.
 * 3. Undo operation      — stores move history.
 */
@SuppressWarnings("unchecked")
public class GameStack<T> {

  private Object[] data;
  private int size;
  private static final int DEFAULT_CAP = 16;

  public GameStack() {
    data = new Object[DEFAULT_CAP];
    size = 0;
  }

  /**
   * Push an item onto the top of the stack.
   */
  public void push(T item) {
    if (size == data.length) {
      grow();
    }
    data[size++] = item;
  }

  /**
   * Pop and return the top item.
   */
  public T pop() {
    if (isEmpty()) {
      throw new RuntimeException("Stack is empty, cannot pop!");
    }
    T item = (T) data[--size];
    data[size] = null;
    return item;
  }

  /**
   * Peek at the top item without removing it.
   */
  public T peek() {
    if (isEmpty()) {
      throw new RuntimeException("Stack is empty!");
    }
    return (T) data[size - 1];
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  /**
   * Clear all elements from the stack.
   */
  public void clear() {
    for (int i = 0; i < size; i++) {
      data[i] = null;
    }
    size = 0;
  }

  /**
   * Copy stack contents to an array (non-destructive).
   */
  public Object[] toArray() {
    Object[] arr = new Object[size];
    System.arraycopy(data, 0, arr, 0, size);
    return arr;
  }

  /**
   * Visual representation of the stack for debugging/display.
   */
  public String visualize(int maxShow) {
    StringBuilder sb = new StringBuilder();
    sb.append("Stack [size=").append(size).append("] ");
    int start = Math.max(0, size - maxShow);
    if (start > 0) {
      sb.append("... ");
    }
    sb.append("bottom[ ");
    for (int i = start; i < size; i++) {
      sb.append(data[i]);
      if (i < size - 1) {
        sb.append(" -> ");
      }
    }
    sb.append(" ]top");
    return sb.toString();
  }

  private void grow() {
    Object[] newData = new Object[data.length * 2];
    System.arraycopy(data, 0, newData, 0, data.length);
    data = newData;
  }
}