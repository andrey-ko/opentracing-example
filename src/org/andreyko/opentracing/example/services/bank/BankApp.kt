package org.andreyko.opentracing.example.services.bank

import org.andreyko.opentracing.example.*

class BankApp(val messageBroker: MessageBroker) {
  val log = System.getLogger(javaClass.name)
  val service = BankService()
  val tracer = App.createTracer(BankModule.address)
  
  fun run() {
    messageBroker.subscribe(
      BankModule.address,
      BankHandler(service, tracer)
    )
  }
}