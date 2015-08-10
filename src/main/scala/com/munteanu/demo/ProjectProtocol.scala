package com.munteanu.demo

import spray.json._

/**
 * Created by romunteanu on 8/3/2015.
 */
object ProjectProtocol extends DefaultJsonProtocol {

  case class Project(id: String, name: String, description: String)

  case object ProjectCreated

  case object ProjectDeleted

  case object ProjectAlreadyExists

  case object ProjectNotFound

  implicit val jsonFormatProject = jsonFormat3(Project.apply)
}
