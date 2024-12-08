package frp.exercise1

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Failure}

given ExecutionContext = ExecutionContext.global

def doInParallel[A, B](a: => A, b: => B): Future[Unit] = {
  val fa = Future(a)
  val fb = Future(b)

  val res = for {
    _ <- fa
    _ <- fb
  } yield ()

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

def printOnComplete[T](future: Future[T]): Unit = {
  future.onComplete {
    case Success(_) => println("Both computations finished successfully.")
    case Failure(exception: Throwable) => println(s"An error occurred: ${exception.getMessage}")
  }
}