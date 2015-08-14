package com.munteanu.demo

import spray.json._

/**
 * Created by romunteanu on 8/3/2015.
 */
object ProjectProtocol extends DefaultJsonProtocol {
  import com.munteanu.demo.domain.Project

  case class ProjectCreated(msg: String = "Project has been successfully created.")

  case class ProjectDeleted(msg: String = "Successful delete operation.")

  case class ProjectAlreadyExists(msg: String = "Project already exists.")

  case class ProjectNotFound(msg: String = "Project not found.")

  implicit val jsonFormatProject = jsonFormat3(Project.apply)
}
