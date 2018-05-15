package org.andreyko.opentracing.example.services.organization

import io.jaegertracing.*
import org.andreyko.opentracing.example.*

class OrganizationHandler(
  val service: IOrganzation,
  tracer: Tracer
) : BaseHandler(tracer) {
  
  override fun invokeBlocked(action: String, vararg args: Any): Any {
    return when (action) {
      "feed-customer" -> {
        service.feedCustomer(args[0] as String)
      }
      else -> throw Exception("unknown action '$action'")
    }
  }
  
}