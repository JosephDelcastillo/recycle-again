package org.teamresistance.frc.subsystem.binliftin;

import org.strongback.command.Command;

import java.util.function.BooleanSupplier;

/**
 * This command will reel the {@link BinLiftin} backwards to unload any totes in the process.
 * It will finish after 10 seconds or when it reaches the home position. Once done, it will idle
 * the motors. If the BinLiftin is already home, the command will abort.
 * <p>
 * It may be desirable to afterward call {@link LiftinZero} to move the tusks up to the zero
 * position, where it will be ready to pick up new totes.
 *
 * @author Rothanak So
 * @implNote This command stops at the <i>home</i> position. It may make more sense to stop
 * at zero, since we are now capable of tracking the position with the {@link TuskWatcher}.
 * In that case, we will need to ensure the command aborts when it's anywhere <i>past</i> zero,
 * i.e. if the {@link BinLiftin} is anywhere between zero and home. See the {@link LiftinZero}
 * command for an example of how this can be done. -- Rothanak
 * @see BinLiftin#unsafeUnload()
 * @see BinLiftin#hold()
 */
class LiftinUnloadAll extends Command {
  private static final int UNLOAD_TIMEOUT_SECONDS = 10;

  private final BinLiftin binLiftin;
  private final BooleanSupplier isAtHome;

  LiftinUnloadAll(BinLiftin binLiftin, BooleanSupplier isAtHome) {
    super(UNLOAD_TIMEOUT_SECONDS, binLiftin);
    this.binLiftin = binLiftin;
    this.isAtHome = isAtHome;
  }

  @Override
  public boolean execute() {
    binLiftin.unsafeGoHome();
    return isAtHome.getAsBoolean();
  }

  @Override
  public void interrupted() {
    this.end();
  }

  @Override
  public void end() {
    binLiftin.hold();
  }
}
