package org.andreyko.opentracing.example

import io.jaegertracing.*
import java.util.concurrent.*

abstract class BaseHandler(
  val tracer: Tracer
) : (Message, (Message) -> Unit) -> Unit {
  val log = System.getLogger(javaClass.name)
  val threadPool = Executors.newFixedThreadPool(10) {
    Thread(it).apply {
      isDaemon = true
    }
  }
  
  protected abstract fun invokeBlocked(action: String, vararg args:Any): Any
  
  @Suppress("UNCHECKED_CAST")
  private fun invokeBlocked(msg: Message): Message {
    val opts = msg.options ?: throw Exception("action not specified")
    val action = opts["action"] ?: throw Exception("action not specified")
    val args = msg.payload as Array<Any>
  
    val traceContext = opts["trace-context"]?.let {
      SpanContext.contextFromString(it)
    }
    
    val scope = tracer.buildSpan(action).apply {
      if(traceContext != null) asChildOf(traceContext)
    }.startActive(true)
    try {
      return Message(invokeBlocked(action, *args), null)
    } finally {
      scope.close()
    }
  }
  
  override fun invoke(msg: Message, replyHandler: (Message) -> Unit) = threadPool.execute {
    val reply = try {
      invokeBlocked(msg)
    } catch (ex: Throwable) {
      Message(ex, null)
    }
    try {
      replyHandler(reply)
    } catch (ex: Throwable) {
      log.log(System.Logger.Level.ERROR, "unhandled exception", ex)
    }
  }
}