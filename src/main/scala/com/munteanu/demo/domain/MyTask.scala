package com.munteanu.demo.domain

import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 8/17/2015.
 */
case class MyTask(id: Option[Long] = None, title: String, projectId: Long) {
  override def toString: String = s"MyTask[id=$id, title=$title, projectId=$projectId]"
}

class MyTasks(tag: Tag) extends Table[MyTask](tag, "tasks") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def projectId = column[Long]("project_id")

  def * = (id.?, title, projectId) <> (MyTask.tupled, MyTask.unapply)

  def project = foreignKey("PROJ_FK", projectId, TableQuery[Projects])(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
}