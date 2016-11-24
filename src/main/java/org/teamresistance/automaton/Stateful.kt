package org.teamresistance.automaton

abstract class Stateful<S, E, D>(initialState: S, initialData: D) : EventListener<E> {
    private val stateMachine = StateMachine.Builder<S, E, D>(initialState, initialData).apply { init(this) }.build()

    protected abstract fun init(builder: StateMachine.Builder<S, E, D>)

    override fun invoke(event: E) {
        stateMachine(event)
    }
}