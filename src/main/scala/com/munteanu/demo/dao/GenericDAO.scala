package com.munteanu.demo.dao

import slick.dbio.DBIO
import slick.lifted.TableQuery
import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 9/1/2015.
 */
trait GenericDAO {

}
/*
trait GenericDAO[T <: Table[E] with IdentifiableTable[PK], E <: Entity[PK], PK: BaseColumnType] {

  val tableQuery: TableQuery[T]

//  def $id(table: T): Rep[PK]

  def queryById(id: PK): Query[T, E, Seq] = tableQuery.filter(_.id === id)

  def count(): DBIO[Int] = tableQuery.size.result

  def findAll(): DBIO[Seq[E]] = tableQuery.result

  def findOne(id: PK): DBIO[Option[E]] =  queryById(id).result.headOption

  def delete(id: PK): DBIO[Int] = queryById(id).delete

  def save(en: E): DBIO[Int] = tableQuery.insertOrUpdate(en)
}
*/

trait Entity[PK] {
  def id: Option[PK]
}

trait IdentifiableTable[I] {
  def id: slick.lifted.Rep[I]
}