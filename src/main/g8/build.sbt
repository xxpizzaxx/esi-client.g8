organization  := "$organization$"
name := "$name;format="norm"$"
version := "0.0.1-SNAPSHOT"
scalaVersion = "$scala_version$"

resolvers += Resolver.jcenterRepo

val esiClientVersion = "$esi_client_version$"

libraryDependencies ++= Seq(
  "eveapi" %% "esi-client" % esiClientVersion
)
