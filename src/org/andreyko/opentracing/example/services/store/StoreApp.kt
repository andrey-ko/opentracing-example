package org.andreyko.opentracing.example.services.store

import org.andreyko.opentracing.example.*
import org.andreyko.opentracing.example.services.bank.*

class StoreApp(val messageBroker: MessageBroker) {
  val log = System.getLogger(javaClass.name)
  val service = StoreService(
    BankProxy(messageBroker)
  )
  val tracer = App.createTracer(StoreModule.address)
  
  fun run() {
    messageBroker.subscribe(
      StoreModule.address,
      StoreHandler(service, tracer)
    )
  }
}