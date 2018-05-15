package org.andreyko.opentracing.example.services.store

interface IStore {
  fun createOrder(orderInfo: OrderInfo, paymentInfo: PaymentInfo): String
  fun getProducts(): List<ItemInfo>
  fun getOrders(customerId: String): Map<String, OrderInfo>
}