package org.andreyko.opentracing.example.services.organization

import org.andreyko.opentracing.example.services.bank.*
import org.andreyko.opentracing.example.services.store.*

class OrganizationService(
  val store: IStore,
  val bank: IBank
) : IOrganzation {
  
  override fun feedCustomer(customerId: String) {
    
    val food = store.getProducts().find { it.name == "food" && it.price <= 100.0 } ?: throw Exception("no food")
    
    val accountId = bank.createAccount(customerId)
    bank.deposit(accountId, food.price)
    
    store.createOrder(
      OrderInfo(mapOf(
        Pair(food.itemId, 1)
      )),
      PaymentInfo(
        customerId, accountId
      )
    ).let { orderId ->
      println("order completed (orderId = '$orderId')")
    }
    
    // TODO: wait for delivery
    
  }
}