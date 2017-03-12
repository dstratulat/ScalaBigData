package akkastreams

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}

import scala.concurrent.Future


object SimpleStream extends App {
  // to run a stream you need a materializer,
  // to create a materializer you need an ActorSystem
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 42)
  val flow: Flow[Int, Int, NotUsed] = Flow[Int].map(_ + 1)
  val sink: Sink[Any, Future[Done]] = Sink.foreach(println)
  val graph: RunnableGraph[NotUsed] = source.via(flow).to(sink)

  // materializer is implicitly used here
  graph.run()

  //Nice to know: every class used in this example is a subclass of Graph.

}
