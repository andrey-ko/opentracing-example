package org.andreyko.opentracing.example.services.organization

import io.opentracing.util.*
import kotlinx.coroutines.experimental.*
import org.andreyko.opentracing.example.*
import org.andreyko.opentracing.example.services.bank.*
import org.andreyko.opentracing.example.services.store.*
import java.util.concurrent.*
import kotlin.concurrent.*

class OrganizationService(
  val store: IStore,
  val bank: IBank
) : IOrganzation {
  
  val threadPool = Executors.newCachedThreadPool()
  
  override fun feedCustomer(customerId: String) {
    
    async {
    
    }
    
    val span = App.scopeManager.active().span()
    val foodFuture = threadPool.submit<ItemInfo> {
      App.scopeManager.activate(span, false).use {
        return@submit store.getProducts().find { it.name == "food" && it.price <= 100.0 } ?: throw Exception("no food")
      }
    }
    
    val accountId = bank.createAccount(customerId)
    val food = foodFuture.get()
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