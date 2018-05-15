package org.andreyko.opentracing.example

import io.jaegertracing.*

open class BaseProxy(
  val messageBroker: MessageBroker,
  val address: String
) {
  
  protected fun processReply(reply: Message): Any {
    if (reply.payload is Throwable) throw reply.payload
    return reply.payload
  }
  
  protected fun invokeMethod(action: String, vararg args: Any): Any {
    val opts = HashMap<String, String>()
    opts["action"] = action
    
    val context = App.scopeManager.active()?.let { scope ->
      (scope.span().context() as SpanContext).contextAsString()
    }
    if (context != null) {
      opts["trace-context"] = context
    } else {
      println("no active context")
    }
    
    val request = Message(args, opts)
    
    val sync = Object()
    var result: Message? = null
    messageBroker.send(address, request) { reply ->
      synchronized(sync) {
        result = reply
        sync.notify()
      }
    }
    synchronized(sync) {
      if (result === null) {
        sync.wait()
      }
    }
    return processReply(result!!)
  }
}