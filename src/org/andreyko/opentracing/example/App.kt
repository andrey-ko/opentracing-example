package org.andreyko.opentracing.example

import io.jaegertracing.*
import io.jaegertracing.reporters.*
import io.jaegertracing.samplers.*
import io.jaegertracing.senders.*
import io.opentracing.util.*
import org.andreyko.opentracing.example.services.bank.*
import org.andreyko.opentracing.example.services.organization.*
import org.andreyko.opentracing.example.services.store.*

object App {
  val log = System.getLogger(javaClass.name)
  
  val scopeManager = ThreadLocalScopeManager()
  
  @JvmStatic
  fun createTracer(serviceName: String): Tracer {
    //val reporter = InMemoryReporter();
    val sender = UdpSender("localhost", 5775, 0)
    //val metrics = Metrics(NoopMetricsFactory())
    val reporter = RemoteReporter.Builder()
      .withSender(sender)
      .withFlushInterval(100)
      .withMaxQueueSize(100)
      .build()
    val sampler = ConstSampler(true)
    
    val tracer = Tracer.Builder(serviceName)
      .withScopeManager(scopeManager)
      .withReporter(CompositeReporter(reporter, LoggingReporter()))
      .withSampler(sampler)
      .build()
    
    return tracer
  }
  
  @JvmStatic
  fun server(messageBroker: MessageBroker) {
    BankApp(messageBroker).run()
    StoreApp(messageBroker).run()
    OrganizationApp(messageBroker).run()
  }
  
  @JvmStatic
  fun client(messageBroker: MessageBroker) {
    val organizationService: IOrganzation = OrganizationProxy(messageBroker)
  
    organizationService.feedCustomer("robert-leon")
  }
  
  @JvmStatic
  fun main(vararg args: String) {
    println("java version: ${System.getProperty("java.version")}")
    try {
      val messageBroker = MessageBroker()
      
      server(messageBroker)
      
      client(messageBroker)
      
      println("done")
    } finally {
      Thread.sleep(2000)
    }
    
  }
}