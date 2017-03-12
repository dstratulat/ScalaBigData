package akkastreams.actor

import akka.actor.Actor
import akkastreams.message.DoneMessage

/**
  * Created by dorinastratulat on 2017-03-04.
  */
class HelloActor extends Actor {
  override def receive: Receive = {
    case "hello" => println("hello world from actor")
    case DoneMessage       => println("Unknown message!")
  }
}
