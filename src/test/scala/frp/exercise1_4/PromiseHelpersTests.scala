package frp.exercise1_4

import org.scalatest.funspec.AnyFunSpec
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

given ExecutionContext = ExecutionContext.global

class PromiseHelpersTests extends AnyFunSpec {
  
  /*
  * Future.firstCompletedOf takes a sequence of futures and returns a new future that 
  * is completed with the result of the first future in the list that has completed.
  * 
  * In this test, we have a list of workers, each with a name and a duration. We want to
  * find the first worker that is free.
  */
  describe("Exercise 1.4 a) - Demonstrate the use of Future.firstCompletedOf") {
    it("should find the first free worker") {
      val workers = someBusyWorkers()
      val futures = workers.map(worker => Future {
        Thread.sleep(worker.duration)
        worker
      })

      val firstFreeWorker = Future.firstCompletedOf(futures)

      firstFreeWorker.onComplete(worker => println(s"First free worker: $worker"))

      Await.result(firstFreeWorker, Duration.Inf)
      
      assert(firstFreeWorker.isCompleted)
      assert(firstFreeWorker.value.get.isSuccess)
    }
  }

  describe("Exercise 1.4 b) - Implement Future.doCompetitively") {
    it("should return the first successful future") {
      val workers = someBusyWorkers()
      val futures = workers.map(worker => Future {
        Thread.sleep(worker.duration)
        worker
      })

      val firstFreeWorker = futures.doCompetitively()

      firstFreeWorker.onComplete(worker => println(s"First free worker: $worker"))

      Await.result(firstFreeWorker, Duration.Inf)

      assert(firstFreeWorker.isCompleted)
      assert(firstFreeWorker.value.get.isSuccess)
    }
  }
}

case class Worker(name: String, duration: Int)

def someBusyWorkers(): Seq[Worker] = {
  val worker1 = Worker("Worker 1", 1000)
  val worker2 = Worker("Worker 2", 500)
  val worker3 = Worker("Worker 3", 2000)
  val worker4 = Worker("Worker 4", 1500)
  val worker5 = Worker("Worker 5", 3000)
  val worker6 = Worker("Worker 6", 2500)
  val worker7 = Worker("Worker 7", 200)
  val worker8 = Worker("Worker 8", 700)
  val worker9 = Worker("Worker 9", 1200)
  val worker10 = Worker("Worker 10", 800)

  Seq(worker1, worker2, worker3)
}
