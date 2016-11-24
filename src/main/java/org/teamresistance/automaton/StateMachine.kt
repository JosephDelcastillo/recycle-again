package org.teamresistance.automaton

import kotlin.reflect.KFunction

class StateMachine<S, in E, D> private constructor(
    initialState: S,
    initialData: D,
    private val states: Map<S, State<D>>,
    private val transitions: Map<S, (E, D) -> Pair<S, D?>>
) : EventListener<E> {
  private var currentState = initialState
  private var currentData = initialData

  override fun invoke(event: E) {
    // Look up the transition function
    val transition = transitions[currentState] ?: error("No transition function matching ($currentState, $event)")

    // Resolve the transition function in order to find our destination state
    val (destinationKey, newData) = transition(event, currentData)
    val destinationState = states[destinationKey] ?: error("Cannot find target state with key $destinationKey")

    // Update the current data and resolve the entry output
    currentData = newData ?: currentData
    destinationState.onEntry(currentData)

    // Set the current state to the target state
    currentState = destinationKey
  }

  private data class State<D>(val onEntry: (D) -> Any)

  class Builder<S, E, D>(private val initialState: S, private val initialData: D) {
    private val states: MutableMap<S, State<D>> = mutableMapOf()
    private val transitions: MutableMap<S, (E, D) -> Pair<S, D?>> = mutableMapOf()
    private val transitionHelperCache: Transition<S, D> = Transition() // to avoid multiple instantiations

    class Transition<S, D> {
      fun goto(targetState: S): Intent<S, D> = Intent(Intent.Type.GoTo(targetState))
      val stay: Intent<S, D> = Intent(Intent.Type.Stay())
    }

    data class Intent<S, D> constructor(val type: Intent.Type<S>, val data: D? = null) {
      @Suppress("unused")
      sealed class Type<S> {
        data class GoTo<S>(val targetState: S) : Type<S>()
        class Stay<S> : Type<S>()
      }

      infix fun using(data: D): Intent<S, D> = this.copy(data = data)
    }

    data class Package<E, D>(val event: E, val data: D) // named Pair<E, D> to provide intuitive getter names

    fun whenState(key: S, onEntry: (D) -> Any = {}, onEvent: Transition<S, D>.(Package<E, D>) -> Intent<S, D> = { stay }) {
      // Add the whenState to the map, and throw an exception if there happened to be a previous state there
      states.put(key, State(onEntry))?.let { error("Attempting to add already-defined state $key.") }

      // Interprets the intent function into a pure transition function understood by the state machine
      val transitionTable: (E, D) -> Pair<S, D?> = { event: E, data: D ->
        val (type, newData) = transitionHelperCache.onEvent(Package(event, data))
        val nextState = when (type) {
          is Intent.Type.Stay -> key
          is Intent.Type.GoTo -> type.targetState
        }
        Pair(nextState, newData)
      }

      // Add a transition table for the specified state, and throw an exception if one was already registered
      transitions.put(key, transitionTable)?.let { error("State $key already has a transition table.") }
    }

    fun build(): StateMachine<S, E, D> {
      return StateMachine(initialState, initialData, states, transitions)
    }
  }
}