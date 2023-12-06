package arithmetic

import munit.FunSuite

trait ArithmeticSuite[E <: Expression[E]](construct: ExpressionConstructors[E])
    extends FunSuite {
  def makeFibonacci(n: Int): E = {
    def loop(count: Int): E =
      count match {
        case 0 => construct.literal(0)
        case 1 => construct.literal(1)
        case n => loop(n - 1) + loop(n - 2)
      }

    loop(n)
  }

  test("Fibonacci numbers are calculated correctly") {
    val n = List.range(1, 16)
    assertEquals(
      n.map(n => makeFibonacci(n).eval),
      List(1.0, 1.0, 2.0, 3.0, 5.0, 8.0, 13.0, 21.0, 34.0, 55.0, 89.0, 144.0,
        233.0, 377.0, 610.0)
    )
  }
}
