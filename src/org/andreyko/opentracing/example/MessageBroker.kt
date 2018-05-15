package org.andreyko.opentracing.example

import java.util.concurrent.*

class MessageBroker() {
  
  val log = System.getLogger(javaClass.name)
  val sync = Any()
  val subscribers = HashMap<String, (Message, (Message) -> Unit) -> Unit>()
  
  private fun doReply(replyCallback: (Message) -> Unit, msg: Message) {
    try {
      replyCallback(msg)
    } catch (ex: Throwable) {
      log.log(System.Logger.Level.ERROR, "unhandled exception", ex)
    }
  }
  
  fun send(address: String, msg: Message, replyCallback: (Message) -> Unit) {
    val handler = synchronized(sync) {
      return@synchronized subscribers[address] ?: throw IllegalStateException("handler for '$address' not found")
    }
    try {
      handler(msg) { replyMsg ->
        doReply(replyCallback, replyMsg)
      }
    } catch (ex: Throwable) {
      doReply(replyCallback, Message(ex, null))
    }
  }
  
  fun subscribe(address: String, handler: (Message, (Message) -> Unit) -> Unit) {
    synchronized(sync) {
      if (subscribers.putIfAbsent(address, handler) !== null) throw IllegalStateException("already subscribed")
    }
  }
  
}