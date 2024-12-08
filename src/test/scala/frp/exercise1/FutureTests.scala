package frp.exercise1

import org.scalatest.funspec.AnyFunSpec
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class FutureTests extends AnyFunSpec {
  describe("doInParallel - Future[Unit]") {
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

  describe("doInParallel - Future[(U, V)] - with FOR strategy") {
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

  describe("doInParallel - Future[(U, V)] - with FLATMAP strategy") {
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

  describe("doInParallel - Future[(U, V)] - with ZIP strategy") {
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
}
