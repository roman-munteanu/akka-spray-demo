package com.munteanu.demo.dao

import com.munteanu.demo.domain.{Person, PersonTable, Role}
import slick.driver.MySQLDriver
import slick.lifted
import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 10/28/2015.
 */
class PersonDAO extends DbTables with GenericDAO[PersonTable, Person, Long] {

  override val tableQuery: lifted.TableQuery[PersonTable] = persons

  override def queryById(id: Long): MySQLDriver.api.Query[PersonTable, Person, Seq] =
    tableQuery.filter(_.id === id)

  // TODO
//  def findAllJoined(): DBIO[Seq[(Person, String)]] = {
//    val query = for {
//      (p, ptr, r) <- persons join personToRole on (_.id === _.personId) join roles on (_._2.roleId === _.id)
//    } yield (p, r)
//    query.result
//  }

  // findAllJoinedMonadic
  def findAllJoined(): DBIO[Seq[(Person, String)]] = {
    val query = for {
      p <- persons
      ptr <- personToRole if p.id === ptr.personId
      r <- roles if ptr.roleId === r.id
    } yield (p, r.name)
    query.result
  }
}
