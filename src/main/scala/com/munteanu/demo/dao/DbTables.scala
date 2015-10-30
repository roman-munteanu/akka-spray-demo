package com.munteanu.demo.dao

import com.munteanu.demo.domain._
import slick.lifted.TableQuery

/**
 * Created by romunteanu on 8/21/2015.
 */
trait DbTables {
  lazy val projects = TableQuery[Projects]
  lazy val myTasks = TableQuery[MyTasks]
  lazy val workingDays = TableQuery[WorkingDayTable]
  lazy val persons = TableQuery[PersonTable]
  lazy val roles = TableQuery[RoleTable]
  lazy val personToRole = TableQuery[PersonToRoleTable]
}
