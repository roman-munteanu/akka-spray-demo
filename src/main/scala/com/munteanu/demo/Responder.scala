package com.munteanu.demo

import akka.actor._
import spray.http.StatusCodes
import spray.routing._

import scala.language.postfixOps

/**
 * Created by romunteanu on 8/4/2015.
 */
class Responder(requestContext: RequestContext) extends Actor with ActorLogging {
  import  com.munteanu.demo.ProjectProtocol._

  def receive = {

    case ProjectCreated => {
      requestContext.complete(StatusCodes.Created)
      self ! PoisonPill
    }

    case ProjectDeleted => {
      requestContext.complete(StatusCodes.OK)
      self ! PoisonPill
    }

    case ProjectAlreadyExists => {
      requestContext.complete(StatusCodes.Conflict)
      self ! PoisonPill
    }
  }
}
