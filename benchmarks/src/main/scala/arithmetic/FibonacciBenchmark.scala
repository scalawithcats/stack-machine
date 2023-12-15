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

import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit.SECONDS

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = SECONDS)
class FibonnaciBenchmark {
  def makeFibonacci[E <: Expression[E]](
      n: Int,
      construct: ExpressionConstructors[E]
  ): E = {
    def loop(count: Int): E =
      count match {
        case 0 => construct.literal(0)
        case 1 => construct.literal(1)
        case n => loop(n - 1) + loop(n - 2)
      }

    loop(n)
  }

  val nFib = 25
  val expected = 75025

  val baseFib = makeFibonacci(nFib, StructuralRecursion.Expression)
  val basicStackFib = makeFibonacci(nFib, BasicStack.Expression).compile
  val optimizedStackFib = makeFibonacci(nFib, OptimizedStack.Expression).compile
  val optimizedStack2Fib =
    makeFibonacci(nFib, OptimizedStack2.Expression).compile
  val optimizedStack3Fib =
    makeFibonacci(nFib, OptimizedStack3.Expression).compile
  val stackCachingFib =
    makeFibonacci(nFib, StackCaching.Expression).compile
  val superInstructionFib =
    makeFibonacci(nFib, SuperInstruction.Expression).compile
  val byteCodeFib =
    makeFibonacci(nFib, ByteCode.Expression).compile
  val algebraicSimplificationFib =
    makeFibonacci(nFib, AlgebraicSimplification.Expression).compile
  val allFib =
    makeFibonacci(nFib, All.Expression).compile
  val directThreadedFib =
    makeFibonacci(nFib, DirectThreaded.Expression).compile
  val directThreaded2Fib =
    makeFibonacci(nFib, DirectThreaded2.Expression).compile
  val directThreaded3Fib =
    makeFibonacci(nFib, DirectThreaded3.Expression).compile

  @Benchmark
  def baseFibBenchmark(): Unit = {
    assert(baseFib.eval == expected)
  }

  @Benchmark
  def basicStackFibBenchmark(): Unit = {
    assert(basicStackFib.eval == expected)
  }

  @Benchmark
  def optimizedStackFibBenchmark(): Unit = {
    assert(optimizedStackFib.eval == expected)
  }

  @Benchmark
  def optimizedStack2FibBenchmark(): Unit = {
    assert(optimizedStack2Fib.eval == expected)
  }

  @Benchmark
  def optimizedStack3FibBenchmark(): Unit = {
    assert(optimizedStack3Fib.eval == expected)
  }

  @Benchmark
  def stackCachingFibBenchmark(): Unit = {
    assert(stackCachingFib.eval == expected)
  }

  @Benchmark
  def superInstructionFibBenchmark(): Unit = {
    assert(superInstructionFib.eval == expected)
  }

  @Benchmark
  def byteCodeFibBenchmark(): Unit = {
    assert(byteCodeFib.eval == expected)
  }

  @Benchmark
  def algebraicSimplificationFibBenchmark(): Unit = {
    assert(algebraicSimplificationFib.eval == expected)
  }

  @Benchmark
  def allFibBenchmark(): Unit = {
    assert(allFib.eval == expected)
  }

  @Benchmark
  def directThreadedFibBenchmark(): Unit = {
    assert(directThreadedFib.eval == expected)
  }

  @Benchmark
  def directThreaded2FibBenchmark(): Unit = {
    assert(directThreaded2Fib.eval == expected)
  }

  @Benchmark
  def directThreaded3FibBenchmark(): Unit = {
    assert(directThreaded3Fib.eval == expected)
  }
}
