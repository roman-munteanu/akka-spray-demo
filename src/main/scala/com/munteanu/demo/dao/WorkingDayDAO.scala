package com.munteanu.demo.dao

import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.domain.{WorkingDayTable, WorkingDay}
import slick.driver.MySQLDriver.api._
import slick.driver.MySQLDriver.backend.DatabaseDef
import slick.lifted

import scala.concurrent.Future

/**
 * Created by romunteanu on 8/24/2015.
 */
class WorkingDayDAO extends DbTables with GenericDAO[WorkingDayTable, WorkingDay, Long] { // extends DbConfig

//  def findAllJoined()(implicit db: DatabaseDef): Future[Seq[(WorkingDay, String)]] = {
//    val query = for {
//      (wd, p) <- workingDays join projects on (_.projectId === _.id)
//    } yield (wd, p.name)
//    db.run(query.result)
//  }

  def findAllJoined(): DBIO[Seq[(WorkingDay, String)]] = {
    val query = for {
      (wd, p) <- workingDays join projects on (_.projectId === _.id)
    } yield (wd, p.name)
    query.result
  }

  override val tableQuery: lifted.TableQuery[WorkingDayTable] = workingDays

  override def queryById(id: Long): Query[WorkingDayTable, WorkingDay, Seq] = tableQuery.filter(_.id === id)
}
