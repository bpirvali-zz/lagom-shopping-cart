package com.nfl.de.shoppingcart.impl

import java.net.{HttpURLConnection, URL}

import com.lightbend.lagom.scaladsl.api.transport.ResponseHeader
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{Assertion, AsyncWordSpec, BeforeAndAfterAll, Matchers}
import com.nfl.de.shoppingcart.api._

import scala.concurrent.Future

class ShoppingcartServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new ShoppingcartApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[ShoppingcartService]

  override protected def afterAll() = server.stop()

//  def buildReq(url: String): HttpURLConnection = {
//    (new URL("http", "localhost", 9000, url).openConnection()).asInstanceOf[HttpURLConnection]
//  }

  val fun1 = (rh: ResponseHeader, list: List[String]) => {
    assert(list==List() && rh.status==200)
  }

  val fun2 = (list: List[String]) => {
    list should === (List())
  }

  val fun3 = (rh: ResponseHeader, res: List[String]) => {
    assert(rh.status==200)
    res
  }

  "ShoppingCart service" should {
      "return an empty list" in {
        client.showCart("123").invoke().map { fun2 }
      }

      "return an empty list with 200" in {
        client.showCart("123").withResponseHeader.invoke()map { fun1.tupled(_) }
      }

      "return an empty list with 200-2" in {
        client.showCart("123").handleResponseHeader {
          fun3(_,_)
          //(rh, res) => fun3(rh, res)
//          case (rh, response) =>
//            assert(rh.status==200)
//            response
        }
          .invoke()
          .map { fun2(_) }
      }

      "should return List('Apples')" in {
        for {
          _ <- client.addToCart("124").invoke(AddToCartRequest("Apples"))
          answer <- client.showCart("124").invoke()
        } yield {
          answer should === (List("Apples"))
        }
      }

//      "return 200 when adding to cart" in {
//          Future[Assertion] {
//            val request = buildReq("/api/cart/23")
//            request.connect()
//            assert(200 == request.getResponseCode)
//          }
//          //1==1
//      }
//    "say hello" in {
//      client.hello("Alice").invoke().map { answer =>
//        answer should ===("Hello, Alice!")
//      }
//    }

//    "allow responding with a custom message" in {
//      for {
//        _ <- client.useGreeting("Bob").invoke(GreetingMessage("Hi"))
//        answer <- client.hello("Bob").invoke()
//      } yield {
//        answer should ===("Hi, Bob!")
//      }
//    }
  }
}
