package com.munteanu.demo.config

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.munteanu.demo.domain.{Project, Projects}
import com.typesafe.config.ConfigFactory
import slick.dbio.DBIO
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.util.Try

/**
 * Created by romunteanu on 8/12/2015.
 */
trait DbConfig {

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

  val projects = TableQuery[Projects]

  // populate the Database
  val setup = DBIO.seq(
//    projects.schema.drop,
    projects.schema.create,

//    projects += Project(Some(1), "Gemheap", "main")
    projects ++= Seq(
      Project(Some(1), "Gemheap", "App website"),
      Project(Some(2), "ChirpExpenses", "Expenses manager")
    )
  )

  val setupFuture = db.run(setup)
}
