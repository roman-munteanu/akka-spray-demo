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

  implicit val executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val projectService = new ProjectDAO

  var projectsData = Vector[Project](Project(Some(1), "First project", "First description"))

  def routes: Route =
    path("app") {
      get {
        respondWithMediaType(`text/html`) {
//          complete {
//            IndexLayout(projectsData)
//          }
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
//          responder ! projects
          projectService.findAll().onComplete {
            case Success(projectsSeq) => responder ! projectsSeq.toVector
            case Failure(ex) => responder ! ProjectNotFound
          }
        } ~
        post {
          entity(as[Project]) { project => requestContext =>
            log.debug(s"SAVE: ${project.toString}")
            val responder = createResponder(requestContext)
//            createProject(project) match {
//              case true => responder ! ProjectCreated
//              case _ => responder ! ProjectAlreadyExists
//            }
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
//            deleteProject(id)
//            responder ! ProjectDeleted
            projectService.delete(id.toLong).onComplete {
              case Success(_) => responder ! ProjectDeleted
              case Failure(_) => responder ! ProjectDeleted("Delete operation failed.")
            }
          } ~
          get { requestContext =>
            log.debug(s"GET: $id")
            val responder = createResponder(requestContext)
//            findProjectById(id).map(responder ! _)
//              .getOrElse(responder ! ProjectNotFound)
            projectService.findOne(id.toLong).onComplete {
              case Success(proj) => responder ! proj
              case Failure(ex) => responder ! ProjectNotFound
            }
          }
        }
    }
//      ~ path("") {
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


  private def createResponder(requestContext: RequestContext) = {
    context.actorOf(Props(new Responder(requestContext)))
  }

  // dao
  private def createProject(project: Project): Boolean = {
    val doesNotExist: Boolean = !projectsData.exists(_.name eq project.name)
    if (doesNotExist) projectsData = projectsData :+ project
    doesNotExist
  }

  private def deleteProject(id: String) = {
    projectsData = projectsData.filterNot(_.id.get == id.toLong)
  }

  private def findProjectById(id: String): Option[Project] = {
    projectsData.find(_.id.get == id.toLong)
  }

}

