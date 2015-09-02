package com.munteanu.demo.service

import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.dao.WorkingDayDAO
import com.munteanu.demo.domain.WorkingDay

import scala.concurrent.Future

/**
 * Created by romunteanu on 8/28/2015.
 * By creating a service layer it's possible to work with multiple DAOs transactionally (DBIO.seq(...))
 */
class WorkingDayService(val dao: WorkingDayDAO) extends DbConfig {

  def findAllJoined(): Future[Seq[(WorkingDay, String)]] = {
    db.run(dao.findAllJoined())
  }
}
