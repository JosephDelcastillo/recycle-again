package org.teamresistance.frc.util;

import org.strongback.command.Command;
import org.strongback.command.CommandTester;

/**
 * Boring static methods that we like to reuse across multiple tests--no surprises here.
 *
 * @author Rothanak So
 */
public class CommandUtilities {

  // Prevent instantiation by hiding the constructor
  private CommandUtilities() {
  }

  /**
   * Manually interrupt the current command, which triggers the {@link Command#interrupted()}
   * callback and allows us to verify the end behavior of interrupting a command.
   *
   * @param runner An instance of CommandTester containing the command under test
   */
  public static void interrupt(CommandTester runner) {
    runner.step(0);
    runner.cancel();
    runner.step(0);
  }
}
