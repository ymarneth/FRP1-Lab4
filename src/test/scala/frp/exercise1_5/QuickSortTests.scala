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
    it("should sort integers") {
      given Ordering[Int] = Ordering[Int]

      val intSeq = (1 to 20).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, 10))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with threshold 10: $duration")

      results.foreach(result => assert(result == intSeq.sorted, "Int list should be correctly sorted."))
    }

    def testQuickSortThreshold(threshold: Int, seqLength: Int = 10000): Unit = {
      it(s"should sort integers with threshold $threshold") {
        given Ordering[Int] = Ordering[Int]

        val intSeq = (1 to seqLength).map(_ => scala.util.Random.nextInt(1000))
        val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(intSeq, threshold))
        val duration = Await.result(resultFuture, Duration.Inf)

        assert(QuickSort.quickSortParallel(intSeq, threshold).value.get.get == intSeq.sorted, "Sorting is incorrect.")

        println(s"Average Duration with threshold $threshold: $duration")
      }
    }

    Seq(1, 10, 100, 1000, 5000).foreach { threshold =>
      testQuickSortThreshold(threshold)
    }

    it("should handle empty list") {
      val emptySeq = Seq.empty[Int]
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(emptySeq, 10))
      val (duration, results) = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with threshold 10 for empty list: $duration")
      assert(emptySeq == emptySeq.sorted, "Empty list should be considered sorted.")
    }

    it("should handle a single element list") {
      val singleElementSeq = Seq(42)
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(singleElementSeq, 10))
      val duration = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with threshold 10 for single element list: $duration")
      assert(singleElementSeq == singleElementSeq.sorted, "Single element list should be considered sorted.")
    }

    it("should handle a very large list efficiently") {
      val largeSeq = (1 to 100000).map(_ => scala.util.Random.nextInt(1000))
      val resultFuture = QuickSort.measureTime(QuickSort.quickSortParallel(largeSeq, 1000))
      val duration = Await.result(resultFuture, Duration.Inf)
      println(s"Average Duration with threshold 1000 for large list: $duration")
      assert(largeSeq == largeSeq.sorted, "Large list is not correctly sorted.")
    }
  }
}
