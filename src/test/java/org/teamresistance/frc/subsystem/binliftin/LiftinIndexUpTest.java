package org.teamresistance.frc.subsystem.binliftin;

import org.junit.jupiter.api.Test;
import org.strongback.command.CommandTester;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;
import org.strongback.mock.MockSwitch;

import static org.assertj.core.api.Assertions.assertThat;

class LiftinIndexUpTest {
  private MockMotor binLiftinMotor = Mock.stoppedMotor();
  private MockSwitch hasIndexedSwitch = Mock.notTriggeredSwitch();
  private FakeLiftinWatcher liftinWatcher = new FakeLiftinWatcher();
  private LiftinIndexUp liftinIndexUp = new LiftinIndexUp(binLiftinMotor, hasIndexedSwitch, liftinWatcher);

  @Test
  void whileExecuting_ShouldSetMotorOutput() {
    CommandTester runner = new CommandTester(liftinIndexUp);
    runner.step(0);

    assertThat(binLiftinMotor.getSpeed()).isNotZero();
  }

  @Test
  void whileExecuting_WhenAtMaxIndex_ShouldFinish() {
    liftinWatcher.setCurrentIndex(LiftinWatcher.MAX);

    CommandTester runner = new CommandTester(liftinIndexUp);
    boolean isFinished = runner.step(0);

    assertThat(isFinished).isTrue();
  }

  @Test
  void whileExecuting_WhenAtMaxIndex_ShouldNotSetMotorOutput() {
    liftinWatcher.setCurrentIndex(LiftinWatcher.MAX);

    CommandTester runner = new CommandTester(liftinIndexUp);
    runner.step(0);

    assertThat(binLiftinMotor.getSpeed()).isZero();
  }

  @Test
  void whileExecuting_WhenLimitIsNotPressed_ShouldContinueIndexing() {
    CommandTester runner = new CommandTester(liftinIndexUp);
    runner.step(0);

    hasIndexedSwitch.setNotTriggered();
    boolean isFinished = runner.step(0);

    assertThat(isFinished).isFalse();
  }

  @Test
  void whileExecuting_WhenLimitPressed_ShouldFinish() {
    CommandTester runner = new CommandTester(liftinIndexUp);
    runner.step(0);

    hasIndexedSwitch.setTriggered();
    boolean isFinished = runner.step(0);

    assertThat(isFinished).isTrue();
  }

  @Test
  void whileExecuting_WhenLimitPressed_ShouldStopMotor() {
    CommandTester runner = new CommandTester(liftinIndexUp);
    runner.step(0);

    hasIndexedSwitch.setTriggered();
    runner.step(0);

    assertThat(binLiftinMotor.getSpeed()).isZero();
  }

  @Test
  void interrupt_ShouldStopMotor() {
    CommandTester runner = new CommandTester(liftinIndexUp);
    runner.step(0);
    runner.cancel();
    runner.step(0);

    assertThat(binLiftinMotor.getSpeed()).isZero();
  }
}