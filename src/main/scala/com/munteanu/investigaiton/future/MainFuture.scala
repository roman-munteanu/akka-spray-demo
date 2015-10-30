package com.munteanu.investigaiton.future

import scala.concurrent.{ExecutionContext, Future}

import scala.util.{Success, Failure}

import java.util.concurrent.Executors


/**
 * Created by romunteanu on 9/30/2015.
 */
object MainFuture {
  def main(args: Array[String]) = {

    val execService = Executors.newCachedThreadPool()
    implicit val execContext = ExecutionContext.fromExecutorService(execService)

    case class Boat(id: String, name: String)

    case class Oar(id: String, name: String)

    def fetchOneBoat(boatId: String): Future[Boat] = Future {
      Thread.sleep(2000)
      println("fetching a boat...")
      if (Option(boatId).getOrElse("").trim.isEmpty)
        throw new Exception("ID must not be empty")
      else
        Boat(boatId, "Swimming boat")
    }

    def fetchOneOar(oarId: String): Future[Oar] = Future {
      Thread.sleep(1000)
      println("fetching an oar...")
      if (Option(oarId).getOrElse("").trim.isEmpty)
        throw new Exception("ID must not be empty")
      else
        Oar(oarId, "Stroke oar")
    }


//    fetchOneBoat("123abc") onComplete {
//      case Success(resource) => println(resource)
//      case Failure(ex) => println(ex)
//    }

    def obtainResourcesSequentially(boatId: String, oarId: String) = {
      for {
        boat <- fetchOneBoat(boatId)
        oar <- fetchOneOar(oarId)
      } yield (boat, oar)
    }

    def obtainResourcesAlsoSequentially(boatId: String, oarId: String) = {
      fetchOneBoat(boatId) map { boat =>
        fetchOneOar(oarId) map { oar => (boat, oar)}
      }
    }

    def obtainResourcesInParallel(boatId: String, oarId: String) = {
      val futureResultBoat = fetchOneBoat(boatId)
      val futureResultOar  = fetchOneOar(oarId)
      for {
        boat <- futureResultBoat
        oar <- futureResultOar
      } yield (boat, oar)
    }

    val sampleBoatId = "1a2b"
    val sampleOarId  = "34cdef"

//    println("sequential requests: " + obtainResourcesSequentially(sampleBoatId, sampleOarId))
    println("sequential requests: " + obtainResourcesAlsoSequentially(sampleBoatId, sampleOarId))
//    println("in parallel requests: " + obtainResourcesInParallel(sampleBoatId, sampleOarId))


    execContext.shutdown()
  }
}
