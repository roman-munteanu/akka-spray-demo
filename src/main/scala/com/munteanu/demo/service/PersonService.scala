package com.munteanu.demo.service

import com.munteanu.demo.config.DbConfig
import com.munteanu.demo.dao.PersonDAO
import com.munteanu.demo.domain.{Person, Role}

import scala.concurrent.Future

/**
 * Created by romunteanu on 10/28/2015.
 */
class PersonService(val dao: PersonDAO) extends DbConfig {

  def findAllJoined(): Future[Seq[(Person, String)]] =
    db.run(dao.findAllJoined())


}
