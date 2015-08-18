package com.munteanu.demo.dao

import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.domain.Project
import slick.driver.MySQLDriver.api._
import slick.jdbc.{StaticQuery, GetResult}

import scala.concurrent.Future

/**
 * Created by munteanu on 16.08.15.
 */
class ProjectSQLDAO extends DbConfig {
  
  def findAll(): Future[Seq[Project]] = {
    db.run(sql"SELECT * FROM projects".as[Project])
  }

  def findOne(id: Long): Future[Option[Project]] = {
    val table = "projects"
    db.run(sql"SELECT * FROM #$table WHERE id = $id".as[Project].headOption)
  }

  def delete(id: Long): Future[Int] = {
    db.run(sqlu"DELETE FROM projects WHERE id = $id")
  }

  def save(p: Project): Future[Int] = {
    p.id match {
      case Some(id) => db.run(sqlu"UPDATE projects SET name = ${p.name}, description = ${p.description} WHERE id = $id")
      case _ => db.run(sqlu"INSERT INTO projects (name, description) VALUES (${p.name}, ${p.description})")
    }
  }

  def findByName(name: String): Future[Seq[Project]] = {
    // TODO
    val like = " LIKE '%" + name + "%'"
//    StaticQuery.queryNA[Project]("SELECT * FROM projects WHERE name LIKE '%" + name + "%'")
    db.run(sql""" SELECT * FROM projects WHERE name #$like """.as[Project])
  }

  implicit val getProjectResult = GetResult(r => Project(Some(r.nextLong()), r.nextString(), r.nextString()))
//  implicit val getProjectResult = GetResult(r => Project(Some(r.<<), r.<<, r.<<))
}
