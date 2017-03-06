package akkahttp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

object AkkaHttpHelloWorld extends App {

  implicit val actorSystem = ActorSystem("system")
  implicit val actorMaterializer = ActorMaterializer()

  val route =
    pathSingleSlash {
      get {
         complete {
            "Hello World"
         }
      }
    }

    Http().bindAndHandle(route, "localhost", 8080)

    println("server started at 8080")
}
