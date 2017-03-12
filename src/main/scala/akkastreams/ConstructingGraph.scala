package akkastreams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{Broadcast, GraphDSL, Merge, Sink, Source, Zip}
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.SECONDS
import scala.util.Success

/**
  * http://InfoURL.akka.io/docs/akka/2.4.2/scala/stream/stream-graphs.html#constructing-sources-sinks-and-flows-from-partial-
  *
  * In fact, these concepts can be easily expressed as special cases of a partially connected graph:
  *
  *       Source is a partial graph with exactly one output, that is it returns a SourceShape.
  *       Sink is a partial graph with exactly one input, that is it returns a SinkShape.
  *       Flow is a partial graph with exactly one input and exactly one output, that is it returns a FlowShape.
  *
  *       In order to create a Source from a graph the method Source.fromGraph is used, to use it we must have a
  *       Graph[SourceShape, T]. This is constructed using GraphDSL.create and returning a SourceShape
  *       from the function passed in . The single outlet must be provided to the SourceShape.of method
  *       and will become “the sink that must be attached before this Source can run”.
  */
object ConstructingGraph extends App {

  implicit val system = ActorSystem()
  implicit val mt = ActorMaterializer()
  implicit val timeout = Timeout(5, SECONDS)

  val pairs = Source.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    // prepare graph elements
    val zip = b.add(Zip[Int, Int]())
    def ints = Source.fromIterator(() => Iterator.from(1))

    // connect the graph
    ints.filter(_ % 2 != 0) ~> zip.in0
    ints.filter(_ % 2 == 0) ~> zip.in1

    // expose port
    SourceShape(zip.out)
  })

  val firstPair: Future[(Int, Int)] = pairs.runWith(Sink.head)


  //Ex: Combining Sources and Sinks with simplified API
  val sourceOne = Source(List(1))
  val sourceTwo = Source(List(2))
  val merged = Source.combine(sourceOne, sourceTwo)(Merge(_))

  val mergedResult: Future[Int] = merged.runWith(Sink.fold(0)(_ + _))

  //The same can be done for a Sink[T] but in this case it will be fan-out:
//  val sendRmotely = Sink.actorRef(actorRef, "Done")
//  val localProcessing = Sink.foreach[Int](_ => /* do something usefull */ ())
//
//  val sink = Sink.combine(sendRmotely, localProcessing)(Broadcast[Int](_))
//
//  Source(List(0, 1, 2)).runWith(sink)



}
