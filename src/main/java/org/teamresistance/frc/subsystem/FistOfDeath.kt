package org.teamresistance.frc.subsystem

import edu.wpi.first.wpilibj.Timer
import org.strongback.components.Solenoid
import org.teamresistance.automaton.StateMachine
import org.teamresistance.automaton.Stateful
import org.teamresistance.frc.subsystem.FistOfDeathMachine.*
import org.teamresistance.frc.subsystem.FistOfDeathMachine.Event.Fire
import org.teamresistance.frc.subsystem.FistOfDeathMachine.Event.Update
import org.teamresistance.frc.subsystem.FistOfDeathMachine.State.*
import java.util.function.BooleanSupplier

class FistOfDeathMachine(
    private val trigger: Solenoid,
    private val piston: Solenoid,
    private val canFire: BooleanSupplier
) : Stateful<State, Event, Data>(READY, Data(Timer())) {

  companion object {
    private const val TRIGGER_RESET_SECONDS = 2.0
    private const val PISTON_RESET_SECONDS = 2.0
  }

  enum class State {
    READY, CHARGING, FIRED
  }

  sealed class Event {
    object Fire : Event()
    object Update : Event()
  }

  data class Data(val timer: Timer)

  override fun init(builder: StateMachine.Builder<State, Event, Data>) = with(builder) {

    whenState(READY, { data ->
      trigger.retract()
      piston.retract()
      data.timer.reset()
    }) { (event, data) ->
      when (event) {
        is Fire -> if (canFire.asBoolean) goto(CHARGING) using data.apply { timer.start() } else stay
        else -> stay
      }
    }

    whenState(CHARGING, { trigger.extend() }) { (event, data) ->
      when(event) {
        is Update -> if (data.timer.hasPeriodPassed(PISTON_RESET_SECONDS)) goto(FIRED) else stay
        else -> stay
      }
    }

    whenState(FIRED, { piston.extend() }) // TODO
  }
}

class FistOfDeathController(trigger: Solenoid, piston: Solenoid, canFire: BooleanSupplier) {
  private val machine = FistOfDeathMachine(trigger, piston, canFire)

  fun update() {
    machine(Update)
  }

  fun fire() {
    machine(Event.Fire)
  }
}