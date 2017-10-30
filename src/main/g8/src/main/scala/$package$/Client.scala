package $package$

import eveapi.esi.client._
import eveapi.esi.api._
import eveapi.esi.api.CirceCodecs._
import org.http4s.client.blaze.PooledHttp1Client
import cats.effect._
import io.circe._, io.circe.generic.auto._
import io.circe.java8.time._
import cats.syntax.either._

object Client extends App {
  val httpClient = PooledHttp1Client[IO]()
  val esiClient = new EsiClient("my esi client", client.toHttpService)

  // let's request the API's status
  val statusRequest = StatusApi.getStatus()
  val statusRequestIO = esiClient.run(statusRequest).map(_.map(_.toJson.spaces2))
  println(statusRequestIO.unsafeRunSync)
}
