package com.munteanu.demo.dao

import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.domain.WorkingDay
import slick.driver.MySQLDriver.api._
import slick.driver.MySQLDriver.backend.DatabaseDef

import scala.concurrent.Future

/**
 * Created by romunteanu on 8/24/2015.
 */
class WorkingDayDAO extends DbTables { // extends DbConfig

//  def findAllJoined(): Future[Seq[(WorkingDay, String)]] = {
//    val query = for {
//      (wd, p) <- workingDays join projects on (_.projectId === _.id)
//    } yield (wd, p.name)
//    db.run(query.result)
//  }

  def findAllJoined()(implicit db: DatabaseDef): DBIO[Seq[(WorkingDay, String)]] = {
    val query = for {
      (wd, p) <- workingDays join projects on (_.projectId === _.id)
    } yield (wd, p.name)
    query.result
  }
}
