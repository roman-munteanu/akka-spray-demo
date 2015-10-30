package com.munteanu.demo.domain

import com.munteanu.demo.dao.DbTables
import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 10/28/2015.
 */
case class PersonToRole(personId: Long, roleId: Long) {
  override def toString: String = s"PersonToRole[id=$personId, name=$roleId]"
}

class PersonToRoleTable(tag: Tag) extends Table[PersonToRole](tag, "person_to_role") with DbTables {
  def personId = column[Long]("person_id")
  def roleId   = column[Long]("role_id")

  def pk = primaryKey("PK_person_to_role", (personId, roleId))

  def * = (personId, roleId) <> (PersonToRole.tupled, PersonToRole.unapply)

  def personFK = foreignKey("FK_person_to_role__persons", personId, persons)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
  def roleFK   = foreignKey("FK_person_to_role__roles", roleId, roles)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
}
