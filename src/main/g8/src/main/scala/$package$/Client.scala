package $package$

import eveapi.esi.client._
import eveapi.esi.client.EsiClient._
import eveapi.esi.api.CirceCodecs._
import org.http4s.client.blaze.PooledHttp1Client
import cats.effect._
import io.circe._, io.circe.syntax._, io.circe.generic.auto._
import io.circe.java8.time._
import cats.syntax.either._
import cats.syntax._, cats.implicits._

object Client extends App {
  val httpClient = PooledHttp1Client[IO]()
  val esiClient = new EsiClient[IO]("my esi client", httpClient.toHttpService)

  // let's request the API's status
  val statusRequest = status.getStatus()
  val statusRequestIO = statusRequest.run(esiClient).map(_.right.get.asJson.spaces2)
  println(statusRequestIO.unsafeRunSync)

  // let's compose something more complex
  // how about the top 10 ratting systems augmented with their map data
  val statsRequest = for {
    // let's get the systems with the top 10 NPC kills in the last hour
    kills   <- universe.getUniverseSystemKills().run(esiClient).map(_.right.get.sortBy(0 -_.npc_kills).take(10))
    // and and let's look up their map data
    results <- kills.map{ stats =>
      for {
        system        <- universe.getUniverseSystemsSystemId(stats.system_id).run(esiClient).map(_.right.get)
        constellation <- universe.getUniverseConstellationsConstellationId(system.constellation_id).run(esiClient).map(_.right.get)
        region        <- universe.getUniverseRegionsRegionId(constellation.region_id).run(esiClient).map(_.right.get)
      } yield (stats, system, constellation, region)
    }.sequence
  } yield (results)
  // and let's print them out nicely
  val statsText = statsRequest.unsafeRunSync.map{ case (stats, system, constellation, region) =>
    s"\${system.name} in \${constellation.name} in \${region.name} had \${stats.npc_kills} npc kills in the last hour"
  }.mkString("\n")
  println(statsText)


}
