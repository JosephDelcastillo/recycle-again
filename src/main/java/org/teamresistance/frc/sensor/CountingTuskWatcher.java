package org.teamresistance.frc.sensor;

import org.strongback.DataRecordable;
import org.strongback.DataRecorder;
import org.strongback.components.Motor;
import org.strongback.components.Switch;
import org.teamresistance.frc.subsystem.binliftin.BinLiftin;

import edu.wpi.first.wpilibj.Encoder;

/**
 * A TuskWatcher is used to determine the current state of the {@link BinLiftin} machine at any
 * given time. This implementation uses knowledge of the current {@link Motor} direction to
 * determine if it should increase or decrease the index when {@link #onIndexed()} is invoked.
 * <p>
 * If the BinLiftin is idle and {@link #onIndexed()} is called, it is assumed that the BinLiftin is
 * slipping and the count is decremented; this is the best we can do with the current hardware. A
 * more reliable implementation of TuskWatcher will likely use an {@link Encoder} with a k value.
 * <p>
 * <b>Note:</b> Be sure to register a {@link Switch} (or something) to call {@link #onIndexed()}!
 *
 * @author Rothanak So
 */
public class CountingTuskWatcher implements TuskWatcher, DataRecordable {
  private final Motor binLiftinMotor;
  private int currentIndex = 0; // Starts at the zero position

  public CountingTuskWatcher(Motor binLiftinMotor) {
    this.binLiftinMotor = binLiftinMotor;
  }

  public void onIndexed() {
    if (binLiftinMotor.getDirection() == Motor.Direction.FORWARD) {
      currentIndex++; // Motor is going forward, so we've indexed up
    } else {
      currentIndex--; // Motor is reversed or slipping, so we've indexed down
    }

    // The current index logically can't be lower than the home position (-1) because the limit
    // switches can't be activated while the tusks are away. If the current index somehow does
    // become smaller than -1, it is a cause for concern and should be investigated.
    if (currentIndex < -1) {
      System.out.println("Warning: TuskWatcher index below -1; truncating.");
      currentIndex = -1;
    }

    // Safety assurance; also a cause for concern if this code is ever reached.
    if (currentIndex > TuskWatcher.MAX_INDEX) {
      System.out.println("Warning: TuskWatcher index above max (" + MAX_INDEX + "); truncating.");
      currentIndex = TuskWatcher.MAX_INDEX;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getCurrentIndex() {
    return currentIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getCurrentToteCount() {
    return 0; // TODO
  }

  @Override
  public void registerWith(DataRecorder recorder, String name) {
    recorder.register("Current index", () -> currentIndex);
  }
}
