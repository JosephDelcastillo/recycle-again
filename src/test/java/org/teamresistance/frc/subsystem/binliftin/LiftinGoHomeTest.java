package org.teamresistance.frc.subsystem.binliftin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.strongback.command.CommandTester;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;
import org.strongback.mock.MockSwitch;
import org.teamresistance.frc.util.CommandUtilities;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test that verifies the logic for the {@link LiftinGoHome} command with a
 * real {@link BinLiftin} instance. All other collaborators (sensors, motors) are mocked.
 *
 * @author Rothanak So
 * @see BinLiftinTest
 * @see LiftinIndexUpTest
 */
class LiftinGoHomeTest {
  private final MockMotor motor = Mock.stoppedMotor();
  private final MockSwitch atHomeSwitch = Mock.notTriggeredSwitch();
  private final FakeTuskWatcher tuskWatcher = FakeTuskWatcher.atZero();
  private final BinLiftin binLiftin = new BinLiftin(motor, tuskWatcher);
  private final LiftinGoHome goHomeCommand = new LiftinGoHome(binLiftin, atHomeSwitch);

  @Test
  void whenMotorRunning_Interrupt_ShouldKillMotor() {
    motor.setSpeed(1.0);
    CommandTester runner = new CommandTester(goHomeCommand);
    CommandUtilities.interrupt(runner);
    assertThat(motor.getSpeed()).isZero();
  }

  @Nested
  class WhenLimitHit {

    @BeforeEach
    void hitLimit() {
      motor.setSpeed(1.0);
      atHomeSwitch.setTriggered();
    }

    @Test
    void execute_ShouldFinish() {
      CommandTester runner = new CommandTester(goHomeCommand);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isTrue();
    }

    @Test
    void execute_ShouldKillMotor() {
      CommandTester runner = new CommandTester(goHomeCommand);
      runner.step(0);
      assertThat(motor.getSpeed()).isZero();
    }
  }

  @Nested
  class WhenLimitNotYetHit {

    @BeforeEach
    void doNotHitLimit() {
      motor.setSpeed(1.0);
      atHomeSwitch.setNotTriggered();
    }

    @Test
    void execute_ShouldContinue() {
      CommandTester runner = new CommandTester(goHomeCommand);
      boolean isFinished = runner.step(0);
      assertThat(isFinished).isFalse();
    }

    @Test
    void execute_ShouldMoveMotorBackward() {
      CommandTester runner = new CommandTester(goHomeCommand);
      runner.step(0);
      assertThat(motor.getSpeed()).isNegative();
    }
  }
}