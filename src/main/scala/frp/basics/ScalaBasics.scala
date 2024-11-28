package frp.basics

import scala.language.implicitConversions

object ScalaBasics:

  private def implicitConversion(): Unit = {
    val numbers = intWrapper(1) to 3

    println(s"numbers = $numbers")

    case class MyInt(value: Int):
      def isBig: Boolean = value >= 1000

    given Conversion[Int, MyInt] = i => MyInt(i)

    println(s"1000.isBig = ${1000.isBig}")
    println(s"1.isBig = ${1.isBig}")
  }

  private def extensionMethods(): Unit = {
    extension (i: Int)
      private def isEven: Boolean = i % 2 == 0
      private def isOdd: Boolean = i % 2 != 0

    println(s"2.isEven = ${1.isEven}")
    println(s"2.isOdd = ${1.isOdd}")
  }

  private def currying(): Unit = {
    val numbers = 1 to 3

    val sumSquares = numbers.foldLeft(0)((sum, i) => sum + i * i)
    println(s"sumSquares = $sumSquares")

    val foldLeft = numbers.foldLeft("0")((s, i) => s"f($s,$i)")
    println(s"foldLeft = $foldLeft")

    val foldRight = numbers.foldRight("0")((s, i) => s"f($s,$i)")
    println(s"foldRight = $foldRight")

    val f1: ((String, Int) => String) => String = numbers.foldLeft("0")
    val f2 = f1((s, i) => s"f($s,$i)")
    println(s"f2 = $f2")
  }

  private object MyOrderings {
    trait IntOrdering:
      def less(a: Int, b: Int): Boolean

    object AscendingIntOrdering extends IntOrdering:
      override def less(a: Int, b: Int): Boolean = a < b

    object DescendingIntOrdering extends IntOrdering:
      override def less(a: Int, b: Int): Boolean = a > b
  }

  private def implicitParameters(): Unit = {
    import MyOrderings._

    implicit val defaultOrdering: IntOrdering = AscendingIntOrdering

    def min(a: Int, b: Int)(implicit ord: IntOrdering): Int =
      if ord.less(a, b) then a else b

    println(s"min(-3,4) = ${min(-3, 4)(AscendingIntOrdering)}")
    println(s"min(-3,4) = ${min(-3, 4)(DescendingIntOrdering)}")
  }

  private def givens(): Unit = {
    import MyOrderings._

    given IntOrdering with
      override def less(a: Int, b: Int): Boolean = math.abs(a) < math.abs(b)

    def min(a: Int, b: Int)(using ord: IntOrdering): Int =
      if ord.less(a, b) then a else b

    println(s"min(-3,4) = ${min(-3, 4)}")
    println(s"min(-5,4) = ${min(-5, 4)}")
    println(s"min(-3,4) = ${min(-3, 4)(using DescendingIntOrdering)}")
  }

  private def callByName(): Unit = {
    def measure(code: => Unit): Double =
      val start = System.nanoTime()
      code
      System.nanoTime() - start / 1e9

    val time = measure {
      (1 to 100000000).sum
    }

    println(s"time = $time")
  }

  private def companionObject(): Unit = {}

  private def functionTypes(): Unit = {
    val twice = (i: Int) => i * 2
    val twice2: Function1[Int, Int] = (i: Int) => i * 2

    println(s"twice(17) = ${twice(17)}")

    val sign: PartialFunction[Int, 0 | 1 | -1] = {
      case n if n > 0 => 1
      case n if n < 0 => -1
      case _ => 0
    }

    val signFunc: Int => 0 | 1 | -1 = sign

    println(s"sign(17) = ${sign(17)}")
    println(s"sign(-17) = ${sign(-17)}")
    println(s"sign.isDefined(0) = ${sign.isDefinedAt(0)}")
  }

  def main(args: Array[String]): Unit =
    println("========= implicitConversion ======")
    implicitConversion()

    println("========= extensionMethods ======")
    extensionMethods()

    println("========= currying ======")
    currying()

    println("========= implicitParameters ======")
    implicitParameters()

    println("========= givens ======")
    givens()

    println("========= callByName ======")
    callByName()

    println("========= companionObject ======")
    companionObject()

    println("========= functionTypes ======")
    functionTypes()
  end main

end ScalaBasics

