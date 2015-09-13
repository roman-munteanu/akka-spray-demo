package com.munteanu.demo.dao

import com.munteanu.demo.domain.{MyEntity, IdentifiableTable}
import slick.lifted.TableQuery
import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 9/1/2015.
 */
trait GenericDAO[T <: Table[E] with IdentifiableTable[PK], E <: MyEntity[PK], PK] {

  val tableQuery: TableQuery[T]

  def queryById(id: PK): Query[T, E, Seq] // = tableQuery.filter(_.id === id)

  def count(): DBIO[Int] = tableQuery.size.result

  def findAll(): DBIO[Seq[E]] = tableQuery.result

  def findOne(id: PK): DBIO[Option[E]] =  queryById(id).result.headOption

  def delete(id: PK): DBIO[Int] = queryById(id).delete

  def save(en: E): DBIO[Int] = tableQuery.insertOrUpdate(en)

}

