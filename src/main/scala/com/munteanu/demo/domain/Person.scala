package com.munteanu.demo.domain

import slick.driver.MySQLDriver.api._
import java.util.Date

/**
 * Created by romunteanu on 10/28/2015.
 */
case class Person (
  id: Option[Long],
  firstName: String,
  lastName: String,
  email: String,
  password: String,
  dateOfBirth: Option[Date]
) extends MyEntity[Long] {

//  def roles: Seq[Role]
//  def addRole(role: Role) = roles :+ role

  override def toString: String =
    s"Person[id=$id, firstName=$firstName, lastName=$lastName, email=$email, password=$password, dateOfBirth=$dateOfBirth]"
}

//object Person {
//  def apply(id: Option[Long],
//            firstName: String,
//            lastName: String,
//            email: String,
//            password: String,
//            dateOfBirth: Option[Date]) = Person(id, firstName, lastName, email, password, dateOfBirth)
//}

class PersonTable(tag: Tag) extends Table[Person](tag, "persons") with IdentifiableTable[Long] {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def email = column[String]("email")
  def password = column[String]("password")
  def dateOfBirth = column[Option[java.util.Date]]("date_of_birth")

  // in case of using a companion object ((Person.apply _).tupled, Person.unapply)
  def * = (id.?, firstName, lastName, email, password, dateOfBirth) <> (Person.tupled, Person.unapply)

  implicit val dateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    ud => new java.sql.Date(ud.getTime),
    sd => new java.util.Date(sd.getTime))
}


