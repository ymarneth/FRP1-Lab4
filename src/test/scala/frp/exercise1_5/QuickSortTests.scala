package frp.exercise1_5

import frp.exercise1_5.QuickSort.quickSort
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.shouldEqual

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class QuickSortTests extends AnyFunSpec {
  describe("Exercise 1.5 QuickSortTests") {
    it("should sort integers in ascending order") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = Seq(5, 2, 9, 1, 5, 6)
      quickSort(intSeq) shouldEqual Seq(1, 2, 5, 5, 6, 9)
    }

    it("should sort integers in descending order") {
      given Ordering[Int] = Ordering[Int].reverse

      val intSeq = Seq(5, 2, 9, 1, 5, 6)
      quickSort(intSeq) shouldEqual Seq(9, 6, 5, 5, 2, 1)
    }

    it("should sort strings lexicographically") {
      given Ordering[String] = Ordering[String]

      val stringSeq = Seq("apple", "banana", "cherry", "date")
      quickSort(stringSeq) shouldEqual Seq("apple", "banana", "cherry", "date")
    }

    it("should sort strings by length") {
      given Ordering[String] = (x: String, y: String) => x.length compare y.length

      val stringSeq = Seq("apple", "banana", "cherry", "date")
      quickSort(stringSeq) shouldEqual Seq("date", "apple", "banana", "cherry")
    }

    it("should sort enums by ordinal") {
      enum Color:
        case Red, Green, Blue

      given Ordering[Color] = Ordering.by(_.ordinal)

      val colorSeq = Seq(Color.Green, Color.Red, Color.Blue)
      quickSort(colorSeq) shouldEqual Seq(Color.Red, Color.Green, Color.Blue)
    }
  }

  describe("Exercise 1.5 QuickSortParallelTests - different threshold values") {
    it("should sort 20 integers") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 20).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 20))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 20: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    it("should sort 20 integers parallel") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 20).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 10))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 20 parallel: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    it("should sort 50 integers") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 50).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 50))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 50: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    it("should sort 50 integers parallel") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 50).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 25))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 50 parallel: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    it("should sort 100 integers") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 100).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 100))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 100: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    it("should sort 100 integers parallel") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 100).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 50))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 100 parallel: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    it("should sort 1000 integers") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 1000).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 1000))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 1000: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    it("should sort 1000 integers parallel") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 1000).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 500))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with 1000 parallel: $duration")

      assert(results.nonEmpty)
      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }
  }
}
