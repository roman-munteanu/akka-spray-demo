package com.munteanu.investigaiton.future

import scala.concurrent.{ExecutionContext, Future, Promise}

import scala.util.{Success, Failure}

import java.util.concurrent.Executors

/**
 * Created by romunteanu on 9/30/2015.
 */
object MainPromise {
  def main(args: Array[String]) = {

    val execService = Executors.newCachedThreadPool()
    implicit val execContext = ExecutionContext.fromExecutorService(execService)

    case class Balance(amount: Int)

    // ATM
    object AutomaticTellerMachine {
      def currentBalance(): Future[Balance] = {
        val promise = Promise[Balance]()
        Future {
          Thread.sleep(1000)
          promise.success(Balance(250))
        }
        promise.future
      }
    }

    AutomaticTellerMachine.currentBalance() onComplete {
      case Success(Balance(amt)) => println(s"amount: $amt")
      case Failure(ex) => println(ex)
    }

    execContext.shutdown()
  }
}
