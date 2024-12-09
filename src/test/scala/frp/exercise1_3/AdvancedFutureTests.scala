package frp.exercise1_3

import frp.exercise1_3.AdvancedFutureHelpers.{parallelMax1, parallelMax2, parallelMax3}
import org.scalatest.funspec.AnyFunSpec
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class AdvancedFutureTests extends AnyFunSpec {
  describe("Exercise 1.3 a) - parallelMax1") {
    it("should return the maximum value from a list of integers") {
      val result = Await.result(parallelMax1(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 5), Duration.Inf)
      assert(result == 10)
    }

    it("should return the maximum value from a list of integers with negative values") {
      val result = Await.result(parallelMax1(List(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), 5), Duration.Inf)
      assert(result == -1)
    }

    it("should return the maximum value from a list of integers with mixed values") {
      val result = Await.result(parallelMax1(List(-1, 2, -3, 4, -5, 6, -7, 8, -9, 10), 5), Duration.Inf)
      assert(result == 10)
    }

    it("should return the maximum value from a list of integers with a single element") {
      val result = Await.result(parallelMax1(List(1), 5), Duration.Inf)
      assert(result == 1)
    }

    it("should return the maximum value from a list of integers when the list size is not divisible by the chunk size") {
      val result = Await.result(parallelMax1(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3), Duration.Inf)
      assert(result == 10)
    }

    it("should throw an exception when the list is empty") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax1(List.empty, 5), Duration.Inf)
      }
    }

    it("should throw an exception when the list is Nil") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax1(Nil, 5), Duration.Inf)
      }
    }

    it("should throw an exception when the chunk size is less than 1") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax1(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), -2), Duration.Inf)
      }
    }
  }

  describe("Exercise 1.3 b) - parallelMax2") {
    it("should return the maximum value from a list of integers") {
      val result = Await.result(parallelMax2(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 5), Duration.Inf)
      assert(result == 10)
    }

    it("should return the maximum value from a list of integers with negative values") {
      val result = Await.result(parallelMax2(List(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), 5), Duration.Inf)
      assert(result == -1)
    }

    it("should return the maximum value from a list of integers with mixed values") {
      val result = Await.result(parallelMax2(List(-1, 2, -3, 4, -5, 6, -7, 8, -9, 10), 5), Duration.Inf)
      assert(result == 10)
    }

    it("should return the maximum value from a list of integers with a single element") {
      val result = Await.result(parallelMax2(List(1), 5), Duration.Inf)
      assert(result == 1)
    }

    it("should return the maximum value from a list of integers when the list size is not divisible by the chunk size") {
      val result = Await.result(parallelMax2(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3), Duration.Inf)
      assert(result == 10)
    }

    it("should throw an exception when the list is empty") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax2(List.empty, 5), Duration.Inf)
      }
    }

    it("should throw an exception when the list is Nil") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax2(Nil, 5), Duration.Inf)
      }
    }

    it("should throw an exception when the chunk size is less than 1") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax2(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), -2), Duration.Inf)
      }
    }
  }

  describe("Exercise 1.3 c) - parallelMax3 with mySequence") {
    it("should return the maximum value from a list of integers") {
      val result = Await.result(parallelMax3(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 5), Duration.Inf)
      assert(result == 10)
    }

    it("should return the maximum value from a list of integers with negative values") {
      val result = Await.result(parallelMax3(List(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), 5), Duration.Inf)
      assert(result == -1)
    }

    it("should return the maximum value from a list of integers with mixed values") {
      val result = Await.result(parallelMax3(List(-1, 2, -3, 4, -5, 6, -7, 8, -9, 10), 5), Duration.Inf)
      assert(result == 10)
    }

    it("should return the maximum value from a list of integers with a single element") {
      val result = Await.result(parallelMax3(List(1), 5), Duration.Inf)
      assert(result == 1)
    }

    it("should return the maximum value from a list of integers when the list size is not divisible by the chunk size") {
      val result = Await.result(parallelMax3(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3), Duration.Inf)
      assert(result == 10)
    }

    it("should throw an exception when the list is empty") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax3(List.empty, 5), Duration.Inf)
      }
    }

    it("should throw an exception when the list is Nil") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax3(Nil, 5), Duration.Inf)
      }
    }

    it("should throw an exception when the chunk size is less than 1") {
      assertThrows[RuntimeException] {
        val result = Await.result(parallelMax3(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), -2), Duration.Inf)
      }
    }
  }
}
