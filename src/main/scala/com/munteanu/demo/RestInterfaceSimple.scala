package com.munteanu.demo

import akka.actor._
import akka.event.slf4j.SLF4JLogging
import akka.util.Timeout
import com.munteanu.demo.domain.Project
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Created by romunteanu on 8/3/2015.
 */
class RestInterfaceSimple extends HttpServiceActor with RestApiSimple {

  override implicit def actorRefFactory = context

  def receive = runRoute(routes)
}

trait RestApiSimple extends HttpService with SLF4JLogging { actor: Actor =>
  import com.munteanu.demo.ProjectProtocol._

  implicit val executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(10 seconds)

  var projectsData = Vector[Project](Project(Some(1), "First project", "First description"))

  def routes: Route =
    path("app") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            IndexLayout(projectsData)
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
            responder ! projectsData
          } ~
            post {
              entity(as[Project]) { project => requestContext =>
                log.debug(s"SAVE: ${project.toString}")
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
              log.debug(s"DELETE: $id")
              val responder = createResponder(requestContext)
              deleteProject(id)
              responder ! ProjectDeleted
            } ~
              get { requestContext =>
                log.debug(s"GET: $id")
                val responder = createResponder(requestContext)
                findProjectById(id).map(responder ! _)
                  .getOrElse(responder ! ProjectNotFound)
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


