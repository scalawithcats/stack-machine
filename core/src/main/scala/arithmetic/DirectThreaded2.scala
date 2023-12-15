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

object DirectThreaded2 {

  enum Expression extends arithmetic.Expression[Expression] {
    case Literal(value: Double)
    case Addition(left: Expression, right: Expression)
    case Subtraction(left: Expression, right: Expression)
    case Multiplication(left: Expression, right: Expression)
    case Division(left: Expression, right: Expression)

    def +(that: Expression): Expression = Addition(this, that)
    def *(that: Expression): Expression = Multiplication(this, that)
    def -(that: Expression): Expression = Subtraction(this, that)
    def /(that: Expression): Expression = Division(this, that)

    def compile: Program = {
      val machine = new StackMachine()
      def loop(expr: Expression): List[Op] =
        expr match {
          case Literal(value) => List(Lit(value))
          case Addition(left, right) =>
            loop(left) ++ loop(right) ++ List(Add)
          case Subtraction(left, right) =>
            loop(left) ++ loop(right) ++ List(Sub)
          case Multiplication(left, right) =>
            loop(left) ++ loop(right) ++ List(Mul)
          case Division(left, right) =>
            loop(left) ++ loop(right) ++ List(Div)
        }

      Program(machine)(loop(this).toArray)
    }

    def eval: Double = compile.eval
  }
  object Expression extends arithmetic.ExpressionConstructors[Expression] {
    def literal(value: Double): Expression = Literal(value)
  }

  final case class Program(machine: StackMachine)(program: Array[Op]) {
    def eval: Double = machine.eval(program)
  }

  final class Env() {
    val stack: Array[Double] = Array.ofDim[Double](256)
    var sp: Int = 0
    var ip: Int = 0
  }

  sealed abstract class Op extends Function1[Env, Unit]
  final case class Lit(value: Double) extends Op {
    def apply(env: Env): Unit = {
      env.stack(env.sp) = value
      env.sp = env.sp + 1
      env.ip = env.ip + 1
    }
  }
  case object Add extends Op {
    def apply(env: Env): Unit = {
      val a = env.stack(env.sp - 1)
      val b = env.stack(env.sp - 2)
      env.stack(env.sp - 2) = (a + b)
      env.sp = env.sp - 1
      env.ip = env.ip + 1
    }
  }
  case object Sub extends Op {
    def apply(env: Env): Unit = {
      val a = env.stack(env.sp - 1)
      val b = env.stack(env.sp - 2)
      env.stack(env.sp - 2) = (a - b)
      env.sp = env.sp - 1
      env.ip = env.ip + 1
    }

  }
  case object Mul extends Op {
    def apply(env: Env): Unit = {
      val a = env.stack(env.sp - 1)
      val b = env.stack(env.sp - 2)
      env.stack(env.sp - 2) = (a * b)
      env.sp = env.sp - 1
      env.ip = env.ip + 1
    }
  }
  case object Div extends Op {
    def apply(env: Env): Unit = {
      val a = env.stack(env.sp - 1)
      val b = env.stack(env.sp - 2)
      env.stack(env.sp - 2) = (a / b)
      env.sp = env.sp - 1
      env.ip = env.ip + 1

    }
  }
  final class StackMachine() {
    final def eval(program: Array[Op]): Double = {
      val env = new Env()

      def trampoline(): Double =
        if env.ip == program.size then env.stack(env.sp - 1)
        else {
          program(env.ip).apply(env)
          trampoline()
        }

      trampoline()
    }
  }
}
