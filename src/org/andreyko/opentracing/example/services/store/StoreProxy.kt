package org.andreyko.opentracing.example.services.store

import org.andreyko.opentracing.example.*

class StoreProxy(
  messageBroker: MessageBroker
) : BaseProxy(messageBroker, StoreModule.address), IStore {
  
  override fun createOrder(orderInfo: OrderInfo, paymentInfo: PaymentInfo): String {
    return invokeMethod("create-order", orderInfo, paymentInfo) as String
  }
  
  @Suppress("UNCHECKED_CAST")
  override fun getOrders(customerId: String): Map<String, OrderInfo> {
    return invokeMethod("get-orders", customerId) as Map<String, OrderInfo>
  }
  
  @Suppress("UNCHECKED_CAST")
  override fun getProducts(): List<ItemInfo> {
    return invokeMethod("get-products") as List<ItemInfo>
  }
}