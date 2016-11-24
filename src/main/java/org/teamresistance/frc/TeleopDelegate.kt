package org.teamresistance.frc

import org.strongback.Strongback
import org.strongback.components.AngleSensor
import org.strongback.components.ui.ContinuousRange
import org.strongback.components.ui.FlightStick
import org.strongback.components.ui.InputDevice
import org.strongback.control.PIDController
import org.strongback.control.SoftwarePIDController
import org.strongback.control.SoftwarePIDController.SourceType.RATE
import org.strongback.hardware.Hardware.HumanInterfaceDevices
import org.teamresistance.eventbus.EventBus
import org.teamresistance.frc.subsystem.DriveSubsystem

internal class TeleopDelegate(
    private val drive: DriveSubsystem,
    private val leftJoystick: FlightStick,
    private val coDriverBox: CoDriverBox,
    private val gyro: AngleSensor
) : RobotDelegate {
  private val bus: EventBus = EventBus()

  private var isCorrecting = false
  private val rotationController: PIDController = let {
    val input = gyro::getAngle
    val output = { speed: Double -> bus.publish(DriveSubsystem.Event.Rotate(speed)) }
    SoftwarePIDController(RATE, input, output)
        .withGains(1.0, 0.0, 0.0)
        .withInputRange(0.0, 360.0)
        .withOutputRange(-1.0, 1.0)
        .withTolerance(0.1)
        .continuousInputs(true)
  }

  class CoDriverBox(port: Int) {
    private val inputDevice: InputDevice = HumanInterfaceDevices.driverStationJoystick(port)
    val rotation: ContinuousRange = inputDevice.getAxis(2).map { it * -180 }
  }

  override fun onInit() {
    Strongback.executor().register(rotationController.executable())

    bus.subscribe(drive)
  }

  override fun onPeriodic() {
    // Basically a state machine!
    if (isCorrecting) {
      if (rotationController.isWithinTolerance) {
        isCorrecting = false
        rotationController.disable()
      }
    } else {
      if (!rotationController.isWithinTolerance) {
        isCorrecting = true
        rotationController.withTarget(coDriverBox.rotation.read()).enable()
      }
    }

    bus.publish(DriveSubsystem.Event.DirectDrive(
        leftJoystick.yaw.read(),
        leftJoystick.pitch.read()
    ))
  }
}
