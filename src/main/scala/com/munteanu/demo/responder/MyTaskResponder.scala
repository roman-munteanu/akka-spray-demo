package com.munteanu.demo.responder

import akka.actor.{Actor, ActorLogging, PoisonPill}
import com.munteanu.demo.domain.MyTask
import com.munteanu.demo.protocol.MyTaskProtocol
import spray.http.StatusCodes
import spray.routing.RequestContext

/**
 * Created by romunteanu on 8/25/2015.
 */
class MyTaskResponder(requestContext: RequestContext) extends Actor with ActorLogging {
  import MyTaskProtocol._
  import spray.httpx.SprayJsonSupport._

  def receive = {
    case joinedTasks: Seq[(MyTask, String)] =>
      requestContext.complete(StatusCodes.OK, joinedTasks)
      self ! PoisonPill
  }
}
