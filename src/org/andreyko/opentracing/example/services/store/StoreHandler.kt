package org.andreyko.opentracing.example.services.store

import io.jaegertracing.*
import org.andreyko.opentracing.example.*

class StoreHandler(
  val service: IStore,
  tracer: Tracer
) : BaseHandler(tracer) {
  
  override fun invokeBlocked(action: String, vararg args: Any): Any {
    return when (action) {
      "get-orders" -> {
        service.getOrders(args[0] as String)
      }
      "create-order" -> {
        service.createOrder(args[0] as OrderInfo, args[1] as PaymentInfo)
      }
      "get-products" -> {
        service.getProducts()
      }
      else -> throw Exception("unknown action '$action'")
    }
  }

}