package org.teamresistance.frc.subsystem.binliftin;

/**
 * This is an in-memory implementation of TuskWatcher that gets injected as a dependency
 * into test objects. It is used to simulate specific preconditions for the unit tests.
 * For usage examples, refer to {@link LiftinIndexUpTest} and {@link BinLiftinTest}.
 * <p>
 * Create a new instance by calling {@link FakeTuskWatcher#atZero()}. Then, the state of
 * then object can be manipulated by invoking methods such as {@link #fillWithTotes()} and
 * {@link #setAtTop()} in accordance with the assumptions of the tests.
 *
 * @author Rothanak So
 */
public class FakeTuskWatcher implements TuskWatcher {
  private int currentIndex;
  private int currentToteCount;

  private FakeTuskWatcher(int currentIndex, int currentToteCount) {
    this.currentIndex = currentIndex;
    this.currentToteCount = currentToteCount;
  }

  /**
   * Creates a new instance of {@link FakeTuskWatcher} with the lifter placed at the
   * home position, holding no totes.
   */
  static FakeTuskWatcher atHome() {
    return new FakeTuskWatcher(-1, 0);
  }

  /**
   * Creates a new instance of {@link FakeTuskWatcher} with the lifter placed at the
   * bottommost (zero) position, holding no totes.
   */
  static FakeTuskWatcher atZero() {
    return new FakeTuskWatcher(0, 0);
  }

  @Override
  public int getCurrentIndex() {
    return currentIndex;
  }

  @Override
  public int getCurrentToteCount() {
    return currentToteCount;
  }

  void fillWithTotes() {
    this.currentIndex = MAX_INDEX;
    this.currentToteCount = MAX_INDEX;
  }

  void empty() {
    this.currentIndex = 0;
    this.currentToteCount = 0;
  }

  void setAtTop() {
    this.currentIndex = MAX_INDEX;
  }

  void setInMiddle() {
    this.currentIndex = MAX_INDEX - 1;
  }

  void setAtZero() {
    this.currentIndex = 0;
  }

  void setAtHome() {
    this.currentIndex = -1;
  }
}
