package akkastreams

import akka.http.scaladsl.Http
import akka.stream.scaladsl.Sink
import akka.http.scaladsl.server.Directives._


object SourceAkkaHTTP {
//  val route =
//    path("data") {
//      post {
//        extractRequest { request =>  //the source is extracted from the request
//
//          val source = request.entity.dataBytes
//          val flow = processing()    //business logic here
//          val sink = Sink.ignore     //do nothing here, just start the pulling
//
//          source.via(flow).runWith(sink)
//
//          // more stuff here
//
//        }
//      }
//
//      Http().bindAndHandle(route, hostname, port)
//
//      def processing() = ???

}
