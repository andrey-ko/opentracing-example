package org.andreyko.opentracing.example.services.store

class OrderRecord (
  val orderId: String,
  val customerId: String,
  val orderInfo: OrderInfo
)
