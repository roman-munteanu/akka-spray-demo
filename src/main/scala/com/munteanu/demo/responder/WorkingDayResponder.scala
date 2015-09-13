package com.munteanu.demo.responder

import akka.actor.{Actor, ActorLogging, PoisonPill}
import com.munteanu.demo.dto.WorkingDayDTO
import com.munteanu.demo.protocol.{WorkingDayProtocol, WorkingDayDTOProtocol}
import spray.http.StatusCodes
import spray.routing._

/**
 * Created by romunteanu on 8/25/2015.
 */
class WorkingDayResponder(requestContext: RequestContext) extends Actor with ActorLogging {
  import WorkingDayDTOProtocol._
  import spray.httpx.SprayJsonSupport._

  def receive = {

    case joinedWorkingDays: Seq[WorkingDayDTO] =>
      requestContext.complete(StatusCodes.OK, joinedWorkingDays)
      self ! PoisonPill

//    import WorkingDayProtocol._
//    case joinedWorkingDays: Seq[(WorkingDay, String)] =>
//      requestContext.complete(StatusCodes.OK, joinedWorkingDays)
//      self ! PoisonPill
  }

}
