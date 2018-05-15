package org.andreyko.opentracing.example.services.bank

import io.jaegertracing.*
import org.andreyko.opentracing.example.*

class BankHandler(
  val service: IBank,
  tracer: Tracer
) : BaseHandler(tracer) {
  
  override fun invokeBlocked(action: String, vararg args: Any): Any {
    return when (action) {
      "get-balance" -> {
        tracer.buildSpan("get-balance").start()
        service.getBalance(args[0] as String)
      }
      "create-account" -> {
        service.createAccount(args[0] as String)
      }
      "deposit" -> {
        service.deposit(args[0] as String, args[1] as Double)
      }
      "withdraw" -> {
        service.withdraw(args[0] as String, args[1] as Double)
      }
      else -> throw Exception("unknown action '$action'")
    }
  }
}