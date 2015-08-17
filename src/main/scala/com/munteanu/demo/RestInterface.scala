package com.munteanu.demo

import akka.actor._
import akka.event.slf4j.SLF4JLogging
import akka.util.Timeout
import com.munteanu.demo.dao.{MyTaskDAO, ProjectSQLDAO, ProjectDAO}
import com.munteanu.demo.domain.{Project, MyTask}
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Success, Failure}

/**
 * Created by romunteanu on 8/3/2015.
 */
class RestInterface extends HttpServiceActor with RestApi {

  override implicit def actorRefFactory = context

  def receive = runRoute(routes)
}

trait RestApi extends HttpService with SLF4JLogging { actor: Actor =>
  import com.munteanu.demo.protocol.ProjectProtocol._
  import com.munteanu.demo.protocol.MyTaskProtocol._

  implicit val executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val projectService = new ProjectDAO
//  val projectService = new ProjectSQLDAO
  val myTaskService = new MyTaskDAO

  def routes: Route =
    path("app") {
      get {
        respondWithMediaType(`text/html`) {
          onComplete(projectService.findAll()) {
            case Success(projects: Seq[Project]) => complete(IndexLayout(projects))
            case Failure(ex)    => complete(s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    } ~
      pathPrefix("css") {
        get {
          getFromResourceDirectory("css")
        }
      } ~
      pathPrefix("src") {
        get {
          getFromResourceDirectory("src")
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
          val responder = createResponder(requestContext)
          myTaskService.findAllJoined().onComplete {
            case Success(joinedTasks) => responder ! joinedTasks
            case Failure(ex) => responder ! TaskNotFound(ex.getMessage)
          }
        }
      }
    }

  private def createResponder(requestContext: RequestContext) = {
    context.actorOf(Props(new Responder(requestContext)))
  }
}

