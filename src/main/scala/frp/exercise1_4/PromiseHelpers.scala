package frp.exercise1_4

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

given ExecutionContext = ExecutionContext.global

extension (f: Future.type)
  def doCompetitively[A](futures: Seq[Future[A]]): Future[A] = {
    if (futures.isEmpty) Future.failed(new IllegalArgumentException("futures should not be empty"))

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
