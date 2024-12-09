package frp.exercise1_4

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

given ExecutionContext = ExecutionContext.global

object FutureExtensions:

  implicit class FutureOps[A](futures: Seq[Future[A]]) {
    def doCompetitively(implicit ec: ExecutionContext): Future[A] = {
      val promise = Promise[A]()

      futures.foreach { future =>
        future.onComplete {
          case Success(value) =>
            if (!promise.isCompleted) promise.success(value)
          case Failure(exception) =>
            if (!promise.isCompleted) promise.failure(exception)
        }
      }

      promise.future
    }
  }

  implicit class FutureCompanionOps(val f: Future.type) extends AnyVal {
    def doCompetitively[A](futures: Seq[Future[A]])(implicit ec: ExecutionContext): Future[A] = {
      FutureOps(futures).doCompetitively
    }
  }
