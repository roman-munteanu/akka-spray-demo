package com.munteanu.demo.rest


import com.munteanu.demo.RestApi
import com.munteanu.demo.domain.Project
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import com.munteanu.demo.rest.fixture._

/**
 * Created by romunteanu on 9/1/2015.
 */
class RestApiSpec
  extends Specification
  with Specs2RouteTest
  with RestApi {
    import com.munteanu.demo.protocol.ProjectProtocol._
    import spray.httpx.SprayJsonSupport._

    override implicit def actorRefFactory = system

    "RestApi" should {
      "return a list of entities on a GET request" in {
        Get("/rest/projects") ~> apiRoutes ~> check {
          status === OK
          val content = responseAs[List[Project]]
          content must not be empty
        }
      }

      "return a single entity on GET by ID request" in {
        Get("/rest/projects/1") ~> apiRoutes ~> check {
          status === OK
          val item = responseAs[Project]
          item must not be empty
          item.id === Some(1)
        }
      }

      "create an entity on POST request" in {
        Post("/rest/projects", NewTestProject) ~> apiRoutes ~> check {
          status === Created
        }
      }

//      "remove an entity by ID on DELETE request" in {
//        Delete("/rest/projects/3") ~> apiRoutes ~> check {
//          status === OK
//        }
//      }
    }
}
