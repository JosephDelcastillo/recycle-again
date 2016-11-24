package org.teamresistance.eventbus

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor

class EventBus {
  private val _stream = PublishProcessor.create<Any>().toSerialized()
  val stream: Flowable<Any> = _stream

  // inlined because we want to take advantage of reified types
  inline fun <reified T : Any> subscribe(noinline listener: (T) -> Unit) {
    stream.ofType(T::class.java).subscribe(listener) // inline function only lets us use public APIs
  }

  fun publish(event: Any) = _stream.onNext(event)
}