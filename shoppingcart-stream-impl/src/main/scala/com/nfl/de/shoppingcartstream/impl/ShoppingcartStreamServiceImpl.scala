package com.nfl.de.shoppingcartstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.nfl.de.shoppingcartstream.api.ShoppingcartStreamService
import com.nfl.de.shoppingcart.api.ShoppingcartService

import scala.concurrent.Future

/**
  * Implementation of the ShoppingcartStreamService.
  */
class ShoppingcartStreamServiceImpl(shoppingcartService: ShoppingcartService) extends ShoppingcartStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(shoppingcartService.hello(_).invoke()))
  }
}
