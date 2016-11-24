package org.teamresistance.frc.subsystem

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.strongback.drive.MecanumDrive
import org.strongback.mock.Mock

/**
 * Integration test for [DriveSubsystem] that tests both the SUT and the
 * underlying [MecanumDrive] behavior.
 */
internal class DirectDriveSubsystemTest {
    private val leftFront = Mock.stoppedMotor()
    private val leftRear = Mock.stoppedMotor()
    private val rightFront = Mock.stoppedMotor()
    private val rightRear = Mock.stoppedMotor()
    private val gyro = Mock.angleSensor()

    private lateinit var driveSubsystem: DriveSubsystem

    @BeforeEach
    fun init() {
        val drive = MecanumDrive(leftFront, leftRear, rightFront, rightRear, gyro)
        this.driveSubsystem = DriveSubsystem(drive)
    }

    @Test
    fun directDriveForward_ShouldMoveForward() {
//      driveSubsystem(DriveSubsystem.Event.DirectDrive(1.0, 1.0))
      val drive = MecanumDrive(leftFront, leftRear, rightFront, rightRear, gyro)
      drive.cartesian(1.0, 0.0, 0.0)

      println("$leftFront $rightFront")
      println("$leftRear $rightRear")
    }

    @Nested
    internal inner class InState_RespectingPID {

        fun givenTargetAngle_ShouldRotateToAngle() {

        }
    }
}