package com.munteanu.demo

import spray.json._

/**
 * Created by romunteanu on 8/3/2015.
 */
object ProjectProtocol extends DefaultJsonProtocol {
  import com.munteanu.demo.domain.Project

  case object ProjectCreated

  case object ProjectDeleted

  case object ProjectAlreadyExists

  case object ProjectNotFound

  implicit val jsonFormatProject = jsonFormat3(Project.apply)
}
