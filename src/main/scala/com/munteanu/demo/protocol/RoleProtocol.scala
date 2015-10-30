package com.munteanu.demo.protocol

import spray.json.DefaultJsonProtocol

/**
 * Created by romunteanu on 10/28/2015.
 */
object RoleProtocol extends DefaultJsonProtocol {
  import com.munteanu.demo.domain.Role

  implicit val jsonFormatRole = jsonFormat2(Role.apply)
}
