package org.teamresistance.automaton

// typealias for (Event) -> Unit
interface EventListener<in E> : (E) -> Unit