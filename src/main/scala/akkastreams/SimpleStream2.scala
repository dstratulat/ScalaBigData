package akkastreams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/** Output:
Learn Akka Stream for great good
 Learn Akka Stream for great good
  Learn Akka Stream for great good
   Learn Akka Stream for great good
    Learn Akka Stream for great good
     Learn Akka Stream for great good
      Learn Akka Stream for great good
  */
object SimpleStream2 extends App {
  implicit val system = ActorSystem()
  implicit val mt = ActorMaterializer()

  import system.dispatcher

  val done =
  //RunnableGGraph, we can run it
  //we feed the source to a sink
  //repeat could be also single
  Source.repeat("Learn Akka Stream for great good")
        .zip(Source.fromIterator(() => Iterator.from(0)))
        .take(7)
        .mapConcat { case (s, n) =>
            val i = " " * n
            f"$i$s%n"
        }
    //    .throttle(1, 500.millis 1, ThrottleMode.Shaping)
        .toMat(Sink.foreach(print))(Keep.right)
        .run()

  done.onComplete(_ => system.terminate())
  Await.ready(system.whenTerminated, Duration.Inf)

}
