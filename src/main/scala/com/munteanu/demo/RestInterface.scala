package com.munteanu.demo

import akka.actor._
import akka.event.slf4j.SLF4JLogging
import akka.util.Timeout
import com.munteanu.demo.ProjectProtocol.ProjectDeleted
import com.munteanu.demo.dao.ProjectDAO
import com.munteanu.demo.domain.Project
import spray.http.MediaTypes._
import spray.http.StatusCodes
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
  import  com.munteanu.demo.ProjectProtocol._

  implicit val timeout = Timeout(10 seconds)

  var projects = Vector[Project](Project(Some(1), "First project", "First description"))

  val projectService = new ProjectDAO
  implicit val executionContext = actorRefFactory.dispatcher

  def routes: Route =

    pathPrefix("rest" / "projects") {
      pathEnd {
        get { requestContext =>
          val responder = createResponder(requestContext)
//          responder ! projects
          projectService.findAll().onComplete {
            case Success(lst) => responder ! lst.toVector
            case Failure(ex) => responder ! ProjectNotFound
          }
        } ~
        post {
          entity(as[Project]) { project => requestContext =>
            log.debug(project.toString)
            val responder = createResponder(requestContext)
            createProject(project) match {
              case true => responder ! ProjectCreated
              case _ => responder ! ProjectAlreadyExists
            }
          }
        }
      } ~
        path(Segment) { id =>
          delete { requestContext =>
            val responder = createResponder(requestContext)
            deleteProject(id)
            responder ! ProjectDeleted
          } ~
          get { requestContext =>
            val responder = createResponder(requestContext)
            findProjectById(id).map(responder ! _)
              .getOrElse(responder ! ProjectNotFound)
          }
        }
    } ~
//      path("") {
//        get {
//          respondWithMediaType(`text/html`) {
//            complete {
//              <html>
//                <head>
//                  <title>Spray Demo</title>
//                  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
//                </head>
//                <body>
//                  <h1>Demo</h1>
//
//                  <fieldset>
//                    <legend>Add project</legend>
//                    <form action="/projects" method="post">
//                      <input name="id" value="" placeholder="id" />
//                      <input name="name" value="" placeholder="name" />
//                      <textarea name="description" placeholder="description"></textarea>
//                      <button type="submit">Submit</button>
//                    </form>
//                  </fieldset>
//
//                </body>
//              </html>
//            }
//          }
//        }
//      }
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
      path("") {
        get {
          respondWithMediaType(`text/html`) {
            complete {
              IndexLayout(projects)
            }
          }
        }
      }

  private def createResponder(requestContext: RequestContext) = {
    context.actorOf(Props(new Responder(requestContext)))
  }

  // dao
  private def createProject(project: Project): Boolean = {
    val doesNotExist: Boolean = !projects.exists(_.name eq project.name)
    if (doesNotExist) projects = projects :+ project
    doesNotExist
  }

  private def deleteProject(id: String) = {
    projects = projects.filterNot(_.id.get == id.toLong)
  }

  private def findProjectById(id: String): Option[Project] = {
    projects.find(_.id.get == id.toLong)
  }

}

