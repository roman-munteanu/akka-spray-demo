package com.munteanu.demo.config

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.munteanu.demo.dao.DbTables
import com.munteanu.demo.domain._
import com.munteanu.demo.utils.Utils._
import com.typesafe.config.ConfigFactory
import slick.dbio.DBIO
import slick.driver.MySQLDriver.api._

import scala.util.Try

/**
 * Created by romunteanu on 8/12/2015.
 */
trait DbConfig extends DbTables {

  val config = ConfigFactory.load()

  lazy val dbDriver = Try(config.getString("db.driver")).getOrElse("com.mysql.jdbc.Driver")
  lazy val dbHost = Try(config.getString("db.host")).getOrElse("localhost")
  lazy val dbPort = Try(config.getInt("db.port")).getOrElse(3306)
  lazy val dbName = Try(config.getString("db.name")).getOrElse("work_report")
  lazy val dbUser = Try(config.getString("db.user")).toOption.orNull
  lazy val dbPassword = Try(config.getString("db.password")).toOption.orNull

  // !! no connection pool
//  val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s".format(dbHost, dbPort, dbName),
//    user = dbUser, password = dbPassword, driver = dbDriver)

  val db = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass(dbDriver)
    ds.setJdbcUrl("jdbc:mysql://%s:%d/%s".format(dbHost, dbPort, dbName))
    ds.setUser(dbUser)
    ds.setPassword(dbPassword)
    Database.forDataSource(ds)
  }

  // populate the Database
  val schemas = projects.schema ++ myTasks.schema ++ workingDays.schema
  val setup = DBIO.seq(

//    myTasks.schema.drop,
//    projects.schema.drop,
//    (myTasks.schema ++ projects.schema).drop,
    schemas.create,

//    projects += Project(Some(1), "Gemheap", "main")
    projects ++= Seq(
      Project(Some(1), "Gemheap", "App website"),
      Project(Some(2), "ChirpExpenses", "Expenses manager")
    ),

    myTasks ++= Seq(
      MyTask(Some(1), "First task", 1),
      MyTask(Some(2), "Second task", 1),
      MyTask(Some(3), "Third task", 2)
    ),

    workingDays ++= Seq(
      WorkingDay(Some(1), 1, string2Date("2015-08-24"), string2DateTime("09:30:00"), string2DateTime("18:00:00"), string2DateTime("00:30:00"), Some("test")),
      WorkingDay(Some(2), 1, string2Date("2015-08-27"), string2DateTime("10:00:00"), string2DateTime("19:00:00"), string2DateTime("01:00:00"), Some("another test"))
    )
  )

  val setupFuture = db.run(setup)
}
