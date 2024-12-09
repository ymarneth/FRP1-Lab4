package frp.exercise1_3

import scala.concurrent.{Future, ExecutionContext}

given ExecutionContext = ExecutionContext.global

object AdvancedFutureHelpers:

  def parallelMax1(intList: List[Int], chunkSize: Int)(implicit ec: ExecutionContext): Future[Int] = {
    for {
      _ <- validateInput(intList, chunkSize)
      chunks = chunkList(intList, chunkSize)
      maxFutures = chunks.map(chunk => Future(chunk.max))
      result <- Future.sequence(maxFutures).map(_.max)
    } yield result
  }

  def parallelMax2(intList: List[Int], chunkSize: Int)(implicit ec: ExecutionContext): Future[Int] = {
    for {
      _ <- validateInput(intList, chunkSize)
      chunks = chunkList(intList, chunkSize)
      maxFutures = chunks.map(chunk => Future(chunk.max))
      result <- Future.foldLeft(maxFutures)(Int.MinValue)((acc, max) => acc.max(max))
    } yield result
  }

  def parallelMax3(intList: List[Int], chunkSize: Int)(implicit ec: ExecutionContext): Future[Int] = {
    for {
      _ <- validateInput(intList, chunkSize)
      chunks = chunkList(intList, chunkSize)
      maxFutures = chunks.map(chunk => Future(chunk.max))
      result <- mySequence(maxFutures).map(_.max)
    } yield result
  }

  private def mySequence[A](futures: List[Future[A]])(implicit ec: ExecutionContext): Future[List[A]] = {
    val initial: Future[List[A]] = Future.successful(List.empty[A])

    futures.foldLeft(initial) { (acc, future) =>
      for {
        results <- acc
        result <- future
      } yield results :+ result
    }
  }

  private def validateInput(intList: List[Int], chunkSize: Int): Future[Unit] = {
    if (intList.isEmpty) {
      return Future.failed(new IllegalArgumentException("List may not be empty."))
    }

    if (chunkSize <= 0) {
      return Future.failed(new IllegalArgumentException("Chunk size must be greater than 0."))
    }

    Future.successful(())
  }

  private def chunkList(intList: List[Int], chunkSize: Int): List[List[Int]] = {
    intList.sliding(chunkSize, chunkSize).toList
  }
