package com.munteanu.demo.dao

import com.munteanu.demo.domain.{MyTasks, Projects}
import slick.lifted.TableQuery

/**
 * Created by romunteanu on 8/21/2015.
 */
trait DbTables {
  lazy val projects = TableQuery[Projects]
  lazy val myTasks = TableQuery[MyTasks]
}
