package org.andreyko.opentracing.example.services.organization

import org.andreyko.opentracing.example.*
import org.andreyko.opentracing.example.services.bank.*
import org.andreyko.opentracing.example.services.store.*

class OrganizationApp(val messageBroker: MessageBroker) {
  val log = System.getLogger(javaClass.name)
  val service = OrganizationService(
    StoreProxy(messageBroker),
    BankProxy(messageBroker)
  )
  val tracer = App.createTracer(OrganizationModule.address)
  
  fun run() {
    messageBroker.subscribe(
      OrganizationModule.address,
      OrganizationHandler(service, tracer)
    )
  }
}