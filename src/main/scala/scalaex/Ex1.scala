package scalaex

object Ex1 extends App {

  def operation (m: Int, n: Int, f: (Int,Int) => Int) = f(m, n)

  println(operation(3,2,(a, b) => a + b))
  println(operation(1,2,(a, b) => a * b))

}
