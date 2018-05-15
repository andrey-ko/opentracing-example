package org.andreyko.opentracing.example.services.organization

import org.andreyko.opentracing.example.*

class OrganizationProxy(
  messageBroker: MessageBroker
) : BaseProxy(messageBroker, OrganizationModule.address), IOrganzation {
  
  override fun feedCustomer(customerId: String) {
    return invokeMethod("feed-customer", customerId) as Unit
  }
}