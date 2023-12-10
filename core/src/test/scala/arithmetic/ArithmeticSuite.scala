/*
 * Copyright 2023 Scala with Cats
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
