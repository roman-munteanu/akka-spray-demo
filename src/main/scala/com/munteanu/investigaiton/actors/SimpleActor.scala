package com.munteanu.investigaiton.actors

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.munteanu.investigaiton.protocol.SimpleProtocol._

/**
 * Created by romunteanu on 9/2/2015.
 */
class SimpleActor extends Actor {

  override def receive: Receive = {
    case RequestMessage(m) => println("RequestMessage received."); println(m)
    case Beta(Alpha(msg)) => println("Beta - Alpha: " + msg); sender() ! Gamma("gamma - response.")
    case _ => throw new Exception("unsupported message.")
  }
}
