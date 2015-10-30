package com.munteanu.investigaiton

import akka.actor.{Props, ActorSystem}
import com.munteanu.investigaiton.actors.SimpleActor
import com.munteanu.investigaiton.protocol.SimpleProtocol._

/**
 * Created by romunteanu on 9/2/2015.
 */
object MainInvestigation {
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("ActorSystemForInvestigation")
    val simpleActor = system.actorOf(Props[SimpleActor], "SimpleActor")

    simpleActor ! RequestMessage("Just a request.")

//    val futureResponse: Future[] = simpleActor ! Beta(Alpha("test"))

    system.shutdown()
  }
}
