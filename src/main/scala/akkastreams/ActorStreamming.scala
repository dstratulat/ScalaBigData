package akkastreams

import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Sink, Source}

/**
  * Created by dorinastratulat on 2017-03-05.
  */
object ActorStreamming extends App {
  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()

  case class Weather(zipCode : String, temperature : Double, raining : Boolean)

  val ref = Source.actorRef[Weather](Int.MaxValue, OverflowStrategy.fail)
    .filter(!_.raining)
    .to(Sink foreach println )
    .run() // in order to "keep" the ref Materialized value instead of the Sink's

  ref ! Weather("02139", 32.0, true)
  ref ! Weather("123", 123.0, false)
}
