package org.andreyko.opentracing.example.services.bank

interface IBank {
  fun getBalance(accountId: String): Double
  fun withdraw(accountId: String, amount: Double): Double
  fun deposit(accountId: String, amount: Double): Double
  fun createAccount(customerId: String): String
}