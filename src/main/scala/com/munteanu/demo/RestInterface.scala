package com.munteanu.demo

import akka.actor._
import akka.event.slf4j.SLF4JLogging
import akka.util.Timeout
import com.munteanu.demo.dao.{WorkingDayDAO, MyTaskDAO, ProjectSQLDAO, ProjectDAO}
import com.munteanu.demo.domain.{WorkingDay, Project, MyTask}
import com.munteanu.demo.dto.WorkingDayDTO
import com.munteanu.demo.layout.IndexLayout
import com.munteanu.demo.responder.{MyTaskResponder, WorkingDayResponder, Responder}
import com.munteanu.demo.service.WorkingDayService
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Success, Failure}

//import spray.httpx.PlayTwirlSupport._

/**
 * Created by romunteanu on 8/3/2015.
 */
class RestInterface extends HttpServiceActor with RestApi {

  override implicit def actorRefFactory = context

  def receive = runRoute(apiRoutes)
}

trait RestApi extends HttpService with SLF4JLogging {
  import com.munteanu.demo.protocol.ProjectProtocol._
  import com.munteanu.demo.protocol.MyTaskProtocol._

  implicit val executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val projectService = new ProjectDAO
//  val projectService = new ProjectSQLDAO
  val myTaskService = new MyTaskDAO
  val workingDayService = new WorkingDayService(new WorkingDayDAO)

  def apiRoutes: Route =
//    pathPrefix("css") {
//      get {
//        getFromResourceDirectory("css")
//      }
//    } ~
//    pathPrefix("src") {
//      get {
//        getFromResourceDirectory("src")
//      }
//    } ~
    pathPrefix("css" / Rest) { filename =>
      getFromResource(s"css/$filename")
    } ~
    pathPrefix("src" / Rest) { filename =>
      getFromResource(s"src/$filename")
    } ~
//      pathPrefix("twirl") {
//        get {
//          respondWithMediaType(`text/html`) {
//            complete {
//              html.index.render()
//            }
//          }
//        }
//      } ~
//      pathPrefix("test") {
//        get {
//          respondWithMediaType(`text/html`) {
//            complete(html.index().toString)
//          }
//        }
//      } ~
    pathPrefix("app") {
      get {
        respondWithMediaType(`text/html`) {
          onComplete(projectService.findAll()) {
            case Success(projects: Seq[Project]) => complete(IndexLayout(projects))
            case Failure(ex)    => complete(s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    } ~
    pathPrefix("rest" / "projects") {
      pathEnd {
        get { requestContext =>
          val responder = createResponder(requestContext)
          projectService.findAll().onComplete {
            case Success(projectsSeq) => responder ! projectsSeq
            case Failure(ex) => responder ! ProjectNotFound
          }
        } ~
        post {
          entity(as[Project]) { project => requestContext =>
            log.debug(s"SAVE: ${project.toString}")
            val responder = createResponder(requestContext)
            projectService.save(project).onComplete {
              case Success(_) => responder ! ProjectCreated
              case Failure(_) => responder ! ProjectCreated("Save operation failed.")
            }
          }
        }
      } ~
        path(Segment) { id =>
          delete { requestContext =>
            log.debug(s"DELETE: $id")
            val responder = createResponder(requestContext)
            projectService.delete(id.toLong).onComplete {
              case Success(_) => responder ! ProjectDeleted
              case Failure(_) => responder ! ProjectDeleted("Delete operation failed.")
            }
          } ~
          get { requestContext =>
            log.debug(s"GET: $id")
            val responder = createResponder(requestContext)
            projectService.findOne(id.toLong).onComplete {
              case Success(Some(proj)) => responder ! proj
              case Failure(ex) => responder ! ProjectNotFound
            }
          }
        }
    } ~
    pathPrefix("rest" / "projects" / "name") {
      path(Segment) { keyword =>
        get { requestContext =>
          val responder = createResponder(requestContext)
          projectService.findByName(keyword).onComplete {
            case Success(projectsSeq) => responder ! projectsSeq
            case Failure(ex) => responder ! ProjectNotFound
          }
        }
      }
    } ~
    pathPrefix("rest" / "tasks") {
      pathEnd {
        get { requestContext =>
          val responder = createMyTaskResponder(requestContext)
          myTaskService.findAllJoined().onComplete {
            case Success(joinedTasks) => responder ! joinedTasks
            case Failure(ex) => responder ! TaskNotFound(ex.getMessage)
          }
        }
      }
    } ~
    pathPrefix("rest" / "days") {
      pathEnd {
        get { requestContext =>
          val responder = createWorkingDayResponder(requestContext)
          workingDayService.findAllJoined().onComplete {
//            case Success(data) => responder ! data
            case Success(data) => responder ! data.foldLeft(ListBuffer[WorkingDayDTO]()) { (acc, t) => acc += WorkingDayDTO(t._1, t._2) }
            case Failure(ex) => responder ! TaskNotFound(ex.getMessage)
          }
        }
      } ~
      path(Segment) { id =>
        get { requestContext =>
          log.debug(s"GET wd: $id")
          val responder = createWorkingDayResponder(requestContext)
          workingDayService.findOne(id.toLong) onComplete {
            case Success(wd) => responder ! wd
            case Failure(ex) => responder ! TaskNotFound(ex.getMessage)
          }
        }
      }
    }

  private def createResponder(requestContext: RequestContext) = {
    actorRefFactory.actorOf(Props(new Responder(requestContext)))
  }

  private def createWorkingDayResponder(requestContext: RequestContext) = {
    actorRefFactory.actorOf(Props(new WorkingDayResponder(requestContext)))
  }

  private def createMyTaskResponder(requestContext: RequestContext) = {
    actorRefFactory.actorOf(Props(new MyTaskResponder(requestContext)))
  }
}

