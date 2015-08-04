package com.munteanu.demo

/**
 * Created by romunteanu on 8/3/2015.
 */
object ProjectProtocol {
  import spray.json._

  case class Project(id: Long, name: String, description: String)

  case object ProjectCreated

  case object ProjectDeleted

  case object ProjectAlreadyExists

  case object ProjectNotFound
}
