package com.munteanu.demo.dao

import scala.concurrent.Future
import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.domain.Project
import shapeless.Functions.fnHListOps
import slick.driver.MySQLDriver.api._
import scala.util.{Success, Failure}

/**
 * Created by romunteanu on 8/12/2015.
 */
class ProjectDAO extends DbConfig {

  def findAll(): Future[Seq[Project]] = {

//    val result: Future[Seq[Project]] = db.run(projects.result)
//    result.onComplete {
//      case Success(lst) => lst
//      case Failure(ex) => Nil
//    }

//    db.run(projects.result).onSuccess {
//      case projects => println()
//    }
//    val q = for (p <- projects) yield p

    db.run(projects.result)
  }
}
