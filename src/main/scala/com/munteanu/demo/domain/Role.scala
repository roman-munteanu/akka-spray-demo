package com.munteanu.demo.domain

import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 10/28/2015.
 */
case class Role(id: Option[Long], name: String) extends MyEntity[Long] {
  override def toString: String = s"Role[id=$id, name=$name]"
}

class RoleTable(tag: Tag) extends Table[Role](tag, "roles") with IdentifiableTable[Long] {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  def * = (id.?, name) <> (Role.tupled, Role.unapply)
}