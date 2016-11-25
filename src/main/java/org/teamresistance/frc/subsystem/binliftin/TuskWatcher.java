package org.teamresistance.frc.subsystem.binliftin;

/**
 * A TuskWatcher is used to determine the current state of the {@link BinLiftin} machine
 * at any given time. Internally, it tracks the position of the tote-bearing tusks and the
 * number of totes being carried. A concrete implementation will likely use a combination
 * of sensors and heuristics.
 *
 * @author Rothanak So
 */
public interface TuskWatcher {
  /**
   * The number of tote-bearing tusks on the {@link BinLiftin} machine. Accordingly, this
   * number should also reflect the total number of totes the robot can hold.
   */
  int MAX_INDEX = 9;

  /**
   * Returns the current position of tusks on the {@link BinLiftin}, where the position is the
   * number of tusks forward. A position of 0 means the BinLifter is at the zero position, where
   * the first tusk is peaking out from the bottom of the ladder. A position of -1 means it is
   * tucked away in its home position. A position of {@link #MAX_INDEX} means all tusks are out.
   *
   * @return the number of forward tusks; a number from -1 to {@link #MAX_INDEX}
   */
  int getCurrentIndex();

  /**
   * Returns the number of physical totes the {@link BinLiftin} is currently bearing.
   *
   * @return the number of totes held; a number from 0 to {@link #MAX_INDEX}
   */
  int getCurrentToteCount();
}
