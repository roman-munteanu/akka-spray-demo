package com.munteanu.demo.dao

import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.domain.{Project, Projects}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
 * Created by romunteanu on 8/12/2015.
 */
class ProjectDAO extends DbConfig {

  def filterQuery(id: Long): Query[Projects, Project, Seq] =
    projects.filter(_.id === id)

  def findAll(): Future[Seq[Project]] = {
//    val q = for (p <- projects) yield p
    db.run(projects.result)
  }

  def findAllAsSeq(): Seq[Project] = {
    db.run(projects.result).asInstanceOf[Seq[Project]]
  }

  def findOne(id: Long): Future[Project] = {
    db.run(filterQuery(id).result.head)
  }

  def delete(id: Long): Future[Int] = {
    db.run(filterQuery(id).delete)
  }

  def save(project: Project): Future[Int] = {
    db.run(projects.insertOrUpdate(project))
  }

  def findByName(name: String): Future[Seq[Project]] = {
    val query = for (
      p <- projects
      if p.name like "%" + name + "%"
    ) yield p
    db.run(query.result)
  }
}
