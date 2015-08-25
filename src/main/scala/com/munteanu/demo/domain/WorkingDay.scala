package com.munteanu.demo.domain

import com.munteanu.demo.dao.DbTables
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._

/**
 * Created by romunteanu on 8/24/2015.
 */
case class WorkingDay(
  id: Option[Long] = None,
  projectId: Long,
  dateOfWork: java.util.Date,
  startTime: DateTime,
  endTime: DateTime,
  breakTime: DateTime,
  description: Option[String]) {
  override def toString: String =
    s"WorkingDay[id=$id, projectId=$projectId, dateOfWork=$dateOfWork, startTime=$startTime, endTime=$endTime, breakTime=$breakTime, description=$description]"
}

class WorkingDayTable(tag: Tag) extends Table[WorkingDay](tag, "working_days") with DbTables {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def projectId = column[Long]("project_id")
  def dateOfWork = column[java.util.Date]("date_of_work")
  def startTime = column[DateTime]("start_time") // O.Default()
  def endTime = column[DateTime]("end_time")
  def breakTime = column[DateTime]("break_time") // TODO default breakTime
  def description = column[Option[String]]("description")

  def * = (id.?, projectId, dateOfWork, startTime, endTime, breakTime, description) <> (WorkingDay.tupled, WorkingDay.unapply)

  def project = foreignKey("WD_PROJ_FK", projectId, projects)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  implicit val dateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    ud => new java.sql.Date(ud.getTime),
    sd => new java.util.Date(sd.getTime))

  implicit val dateTimeMapper = MappedColumnType.base[DateTime, java.sql.Time](
    dt => new java.sql.Time(dt.getMillis),
    t => new DateTime(t.getTime))
}