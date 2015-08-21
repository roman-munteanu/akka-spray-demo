package com.munteanu.demo.domain

import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 8/12/2015.
 */
case class Project(id: Option[Long] = None, name: String, description: String) {
  override def toString: String = s"Project[id=$id, name=$name, description=$description]"
}

//object Project {
//  def apply(id: Long, name: String, desc: String): Project = Project(Some(id), name, desc)
//}

class Projects(tag: Tag) extends Table[Project](tag, "projects") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description") // O.Length(150)

  def * = (id.?, name, description) <> (Project.tupled, Project.unapply)
}