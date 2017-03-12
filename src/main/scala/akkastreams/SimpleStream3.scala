package akkastreams

import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import akka.util.Timeout
import akkastreams.actor.HelloActor
import akkastreams.message.DoneMessage

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.SECONDS


/**
  * Source(1 to 10) which we then wire up to a Sink which adds the numbers coming in.
  */
object SimpleStream3 extends App {

  implicit val system = ActorSystem()
  implicit val mt = ActorMaterializer()
  implicit val timeout = Timeout(5, SECONDS)

  //define source and sink
  val source = Source(1 to 10)
  val sink = Sink.fold[Int, Int](0)(_ + _)

  // connect the Source to the Sink, obtaining a RunnableGraph (2 ways below)
  //defaul is Keep.Left => (NotUsed, Future[Int]) => RunnableGraph[Nothing]
  //changing default to Keep.Right => RunnableGraph[Future[Int]]
  val sumFuture = source.toMat(sink)(Keep.right).run() //see below, this can be replaced with runWith

  // Use the shorthand source.runWith(sink)
  val sumFuture2: Future[Int] = source.runWith(sink)

//  sumFuture.onComplete(_ => system.terminate())
//  sumFuture2.onComplete(_ => system.terminate())

  val sum = Await.result(sumFuture, timeout.duration)
  val sum2 = Await.result(sumFuture2, timeout.duration)
  println(s"source.toMat(sink)(Keep.right) Sum = $sum")
  println(s"source.runWith(sink) Sum = $sum")


  //----------differentSourcesAndSinks
  Source(List(1,2,3)).runWith(Sink.foreach(println))

  Source.single("only one element").runWith(Sink.foreach(println))

  Source.repeat("more elements").take(3).runWith(Sink.foreach(println))

  //----------actor sink
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
  Source(List("hello", "hello"))
    .runWith(Sink.actorRef(helloActor, DoneMessage))

  //---------- future source
  val futureString = Source.fromFuture(Future.successful("Hello Streams!"))
    .toMat(Sink.head)(Keep.right).run()
//val futureString = Source.fromFuture(Future.successful("Hello Streams!"))
//  .runWith(Sink.head)
  val theString = Await.result(futureString, timeout.duration)
  println(s"theString = $theString")


  //--------using map on a source
  val source1 = Source(11 to 16)
  val doubleSource = source1.map(_*2)
  val sink1 = Sink.foreach(println)
    //run it
  val result: Future[Done] = doubleSource.runWith(sink1)

}



