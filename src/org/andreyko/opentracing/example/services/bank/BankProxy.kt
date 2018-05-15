package org.andreyko.opentracing.example.services.bank

import org.andreyko.opentracing.example.*

class BankProxy(
  messageBroker: MessageBroker
) : BaseProxy(messageBroker, BankModule.address), IBank {
  
  override fun getBalance(accountId: String): Double {
    return invokeMethod("get-balance", accountId) as Double
  }
  
  override fun withdraw(accountId: String, amount: Double): Double {
    return invokeMethod("withdraw", accountId, amount) as Double
  }
  
  override fun deposit(accountId: String, amount: Double): Double {
    return invokeMethod("deposit", accountId, amount) as Double
  }
  
  override fun createAccount(customerId: String): String {
    return invokeMethod("create-account", customerId) as String
  }
}