package frp.exercise1_2

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

given ExecutionContext = ExecutionContext.global

object FutureHelpers:

  def doInParallel[A, B](a: => A, b: => B): Future[Unit] = {
    val fa = Future(a)
    val fb = Future(b)

    val res = Future.sequence(Seq(fa, fb)).map(_ => ())

    printOnComplete(res)

    res
  }

  def doInParallel[U, V](u: Future[U], v: Future[V], strategy: "FOR" | "FLATMAP" | "ZIP")(implicit ec: ExecutionContext): Future[(U, V)] = {
    val combined = strategy match {
      case "FOR" =>
        for {
          uu <- u
          vv <- v
        } yield (uu, vv)

      case "FLATMAP" =>
        u.flatMap(uu => v.map(vv => (uu, vv)))

      case "ZIP" =>
        u.zip(v)
    }

    printOnComplete(combined)

    combined
  }

  private def printOnComplete[T](future: Future[T]): Unit = {
    future.onComplete {
      case Success(_) => println("Both computations finished successfully.")
      case Failure(exception: Throwable) => println(s"An error occurred: ${exception.getMessage}")
    }
  }