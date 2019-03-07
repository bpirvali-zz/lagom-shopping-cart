package com.nfl.de.shoppingcart.impl

import akka.{Done, NotUsed}
import com.nfl.de.shoppingcart.api.{AddToCartRequest, ShoppingcartService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.{PersistentEntityRegistry}


/**
  * Implementation of the ShoppingcartService.
  */
class ShoppingcartServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends ShoppingcartService {

  override def addToCart(id: String): ServiceCall[AddToCartRequest, Done] = ServiceCall {
    request =>
      val ref = persistentEntityRegistry.refFor[ShoppingcartEntity](id)
      ref.ask(AddToCartCommand(request.product))
  }

  override def showCart(id: String): ServiceCall[NotUsed, List[String]] = ServiceCall {
    _ =>
      val ref = persistentEntityRegistry.refFor[ShoppingcartEntity](id)
      ref.ask(ShowCartCommand)
  }
}
