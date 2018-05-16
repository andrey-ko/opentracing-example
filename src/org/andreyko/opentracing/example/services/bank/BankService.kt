package org.andreyko.opentracing.example.services.bank

import io.jaegertracing.*
import org.andreyko.opentracing.example.*
import java.util.*

class BankService : IBank {
  
  private val accounts = HashMap<String, AccountRecord>()
  
  override fun getBalance(accountId: String): Double {
    Thread.sleep(10)
    val rec = accounts[accountId] ?: throw Exception("account not found")
    return rec.balance
  }
  
  override fun withdraw(accountId: String, amount: Double): Double {
    Thread.sleep(100)
    
    (App.scopeManager.active().span() as Span).tracer.buildSpan("get-balance").startActive(true).use {
      if (getBalance(accountId) < amount) throw Exception("not enough balance")
    }
    
    val rec = accounts[accountId] ?: throw Exception("account not found")
    rec.balance -= amount
    return rec.balance
  }
  
  override fun deposit(accountId: String, amount: Double): Double {
    Thread.sleep(10)
    val rec = accounts[accountId] ?: throw Exception("account not found")
    rec.balance += amount
    return rec.balance
  }
  
  override fun createAccount(customerId: String): String {
    Thread.sleep(200)
    val accountId = UUID.randomUUID().toString()
    if (accounts.putIfAbsent(accountId, AccountRecord(customerId, 0.0)) != null) throw Exception("account already exists")
    return accountId
  }
}