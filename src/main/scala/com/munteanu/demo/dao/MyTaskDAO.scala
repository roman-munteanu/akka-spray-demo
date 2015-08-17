package com.munteanu.demo.dao

import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.domain.{MyTask, MyTasks}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
 * Created by romunteanu on 8/17/2015.
 */
class MyTaskDAO extends DbConfig {

  def findAllJoined(): Future[Seq[(MyTask, String)]] = {
    val q = for {
      (t, p) <- myTasks join projects on (_.projectId === _.id)
    } yield (t, p.name)
    db.run(q.result)
  }
}
