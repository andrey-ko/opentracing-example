package org.andreyko.opentracing.example.services.store

import org.andreyko.opentracing.example.services.bank.*
import java.util.*
import kotlin.collections.HashMap

class StoreService(
  val bank: IBank
) : IStore {
  
  val orders = HashMap<String, OrderRecord>()
  val products = HashMap<String, ItemInfo>()
  
  init {
    addProduct("food", 35.0)
    addProduct("pillow", 40.0)
    addProduct("pen", 2.0)
    addProduct("chair", 300.0)
  }
  
  private fun addProduct(name: String, price: Double) {
    val id = UUID.randomUUID().toString()
    products[id] = ItemInfo(id, name, price)
  }
  
  override fun createOrder(orderInfo: OrderInfo, paymentInfo: PaymentInfo): String {
    Thread.sleep(20)
    var totalPrice = 0.0
    for((itemId, quantity) in orderInfo.items){
      Thread.sleep(10)
      val itemInfo = products[itemId] ?: throw Exception("item '$itemId' out of stock")
      totalPrice += itemInfo.price * quantity
    }
    val currentBalance = bank.getBalance(paymentInfo.accountId)
    if (currentBalance < totalPrice) {
      throw Exception("not enough money")
    }
    bank.withdraw(paymentInfo.accountId, totalPrice)
  
    val orderId = UUID.randomUUID().toString()
  
    if (orders.put(orderId, OrderRecord(orderId, paymentInfo.customerId, orderInfo)) !== null) {
      throw IllegalStateException("order already exists")
    }
  
    return orderId
  }
  
  override fun getProducts(): List<ItemInfo> {
    Thread.sleep(20)
    return products.values.toList()
  }
  
  override fun getOrders(customerId: String): Map<String, OrderInfo> {
    Thread.sleep(20)
    val result = HashMap<String, OrderInfo>()
    for (orderRecord in orders.values) {
      if (orderRecord.customerId != customerId) continue
      if (result.put(orderRecord.orderId, orderRecord.orderInfo) != null) {
        throw IllegalStateException("duplicated order records")
      }
    }
    return result
  }
}