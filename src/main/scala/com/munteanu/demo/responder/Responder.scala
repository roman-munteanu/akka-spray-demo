package com.munteanu.demo.responder

import akka.actor._
import com.munteanu.demo.domain.Project
import com.munteanu.demo.protocol.ProjectProtocol
import spray.http.StatusCodes
import spray.routing._

import scala.language.postfixOps

/**
 * Created by romunteanu on 8/4/2015.
 */
class Responder(requestContext: RequestContext) extends Actor with ActorLogging {
  import ProjectProtocol._
  import spray.httpx.SprayJsonSupport._

  def receive = {

    case projects: Seq[Project] =>
      requestContext.complete(StatusCodes.OK, projects)
      self ! PoisonPill

    case project: Project =>
      requestContext.complete(StatusCodes.OK, project)
      self ! PoisonPill

    case ProjectCreated =>
      requestContext.complete(StatusCodes.Created)
      self ! PoisonPill

    case ProjectDeleted =>
      requestContext.complete(StatusCodes.OK)
      self ! PoisonPill

    case ProjectAlreadyExists =>
      requestContext.complete(StatusCodes.Conflict)
      self ! PoisonPill
  }
}
