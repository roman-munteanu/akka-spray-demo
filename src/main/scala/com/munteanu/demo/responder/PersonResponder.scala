package com.munteanu.demo.responder

import akka.actor.{Actor, ActorLogging, PoisonPill}
import com.munteanu.demo.domain.{Person, Role}
import com.munteanu.demo.protocol.{PersonProtocol, RoleProtocol}
import spray.http.StatusCodes
import spray.routing._

/**
 * Created by romunteanu on 10/28/2015.
 */
class PersonResponder(requestContext: RequestContext) extends Actor with ActorLogging {
  import PersonProtocol._
  import spray.httpx.SprayJsonSupport._

  def receive = {
    case joinedPersons: Seq[(Person, String)] =>
      requestContext.complete(StatusCodes.OK, joinedPersons)
      self ! PoisonPill
  }
}
