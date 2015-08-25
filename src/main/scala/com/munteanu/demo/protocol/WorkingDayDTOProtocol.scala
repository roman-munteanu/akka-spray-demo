package com.munteanu.demo.protocol

import com.munteanu.demo.domain.WorkingDay
import spray.json._
import spray.httpx.SprayJsonSupport._

/**
 * Created by romunteanu on 8/24/2015.
 */
object WorkingDayDTOProtocol extends DefaultJsonProtocol {
  import com.munteanu.demo.dto.WorkingDayDTO
  import WorkingDayProtocol._

  implicit val jsonFormatWorkingDayDTO = jsonFormat2(WorkingDayDTO.apply)
}
