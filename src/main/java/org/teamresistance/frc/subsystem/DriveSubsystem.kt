package org.teamresistance.frc.subsystem

import org.strongback.drive.MecanumDrive
import org.teamresistance.automaton.StateMachine
import org.teamresistance.automaton.Stateful
import org.teamresistance.frc.subsystem.DriveSubsystem.*
import org.teamresistance.frc.subsystem.DriveSubsystem.Event.DirectDrive
import org.teamresistance.frc.subsystem.DriveSubsystem.Event.Rotate
import org.teamresistance.frc.subsystem.DriveSubsystem.State.INITIAL

class DriveSubsystem(private val drive: MecanumDrive) : Stateful<State, Event, Data>(INITIAL, Data(0.0, 0.0, 0.0)) {

  enum class State {
    INITIAL
  }

  sealed class Event {
    data class DirectDrive(val xSpeed: Double, val ySpeed: Double) : Event()
    data class Rotate(val rotateAmount: Double) : Event()
  }

  data class Data(
      val xSpeed: Double,
      val ySpeed: Double,
      val rotation: Double
  )

  override fun init(builder: StateMachine.Builder<State, Event, Data>) = with(builder) {
    whenState(INITIAL, this@DriveSubsystem::drive) { (event, data) ->
      when (event) {
        is DirectDrive -> stay using data.copy(xSpeed = event.xSpeed, ySpeed = event.ySpeed)
        is Rotate -> stay using data.copy(rotation = event.rotateAmount)
      }
    }
  }

  private fun drive(data: Data) {
    // The roll (x-axis) and pitch (y-axis) of the joystick determine the speed of travel.
    // We don't need the bot to rotate, but if we did, we would increase the rotation speed
    // to 1 and then later decrease it to 0 after reaching our target angle. A constant
    // rotation speed of 1 means the robot will be spinning at full speed indefinitely.
    drive.cartesian(data.xSpeed, data.ySpeed, data.rotation)
  }
}