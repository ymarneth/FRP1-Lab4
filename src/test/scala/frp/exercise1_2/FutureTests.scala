package frp.exercise1_2

import frp.exercise1_2.FutureHelpers.doInParallel
import org.scalatest.funspec.AnyFunSpec
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class FutureTests extends AnyFunSpec {
  describe("Exercise 1.2 a) - doInParallel - Future[Unit]") {
    it("should run computations in parallel and return Future[Unit]") {
      val res = doInParallel(println("A"), println("B"))
      Await.result(res, Duration.Inf)
      assert(res.isCompleted)
    }

    it("should print an error message when a computation fails") {
      val res = doInParallel(println("A"), throw new RuntimeException())

      assertThrows[RuntimeException] {
        Await.result(res, Duration.Inf)
      }

      assert(res.isCompleted)
    }
  }

  describe("Exercise 1.2 b) - doInParallel - Future[(U, V)] - with FOR strategy") {
    it("should run computations in parallel and return Future[(U, V)]") {
      val res = doInParallel(Future.successful("U"), Future.successful("V"), "FOR")
      Await.result(res, Duration.Inf)
      assert(res.isCompleted)
    }

    it("should run computations in parallel and handle failure") {
      val successfulFuture = Future.successful(1)
      val failingFuture = Future.failed[Int](new RuntimeException("Test failure"))
      val combined = doInParallel(successfulFuture, failingFuture, "FOR")

      assertThrows[RuntimeException] {
        Await.result(combined, Duration.Inf)
      }

      assert(combined.isCompleted)
    }
  }

  describe("Exercise 1.2 b) - doInParallel - Future[(U, V)] - with FLATMAP strategy") {
    it("should run computations in parallel and return Future[(U, V)]") {
      val res = doInParallel(Future.successful("U"), Future.successful("V"), "FLATMAP")
      Await.result(res, Duration.Inf)
      assert(res.isCompleted)
    }

    it("should run computations in parallel and handle failure") {
      val successfulFuture = Future.successful(1)
      val failingFuture = Future.failed[Int](new RuntimeException("Future failure"))
      val combined = doInParallel(successfulFuture, failingFuture, "FLATMAP")

      assertThrows[RuntimeException] {
        Await.result(combined, Duration.Inf)
      }

      assert(combined.isCompleted)
    }
  }

  describe("Exercise 1.2 c) doInParallel - Future[(U, V)] - with ZIP strategy") {
    it("should run computations in parallel and return Future[(U, V)]") {
      val res = doInParallel(Future.successful("U"), Future.successful("V"), "ZIP")
      Await.result(res, Duration.Inf)
      assert(res.isCompleted)
    }

    it("should run computations in parallel and handle failure") {
      val successfulFuture = Future.successful(1)
      val failingFuture = Future.failed[Int](new RuntimeException("Future failure"))
      val combined = doInParallel(successfulFuture, failingFuture, "ZIP")

      assertThrows[RuntimeException] {
        Await.result(combined, Duration.Inf)
      }

      assert(combined.isCompleted)
    }
  }

  describe("Exercise 1.2 d) - doInParallel - sort number list in parallel") {
    it("sort in parallel") {
      // Merge the two sublists by applying the O(n) algorithm introduced in a)
      // -> not sure what is meant by this
      val randomNumbers = (1 to 100).map(_ => scala.util.Random.nextInt(1000))

      val res = doInParallel(
        randomNumbers.slice(0, 50).sorted,
        randomNumbers.slice(50, 100).sorted
      )

      Await.result(res, Duration.Inf)
      assert(res.isCompleted)
    }
  }
}
