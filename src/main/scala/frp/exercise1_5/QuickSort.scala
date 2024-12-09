package frp.exercise1_5

import scala.concurrent.duration.{Duration, DurationLong}
import scala.concurrent.{ExecutionContext, Future}

given ExecutionContext = ExecutionContext.global

object QuickSort:

  def quickSort[T](seq: Seq[T])(using ord: Ordering[T]): Seq[T] =
    if seq.length <= 1 then seq
    else
      val pivot = seq(seq.length / 2)
      Seq.concat(quickSort(seq filter (ord.lt(_, pivot))),
        seq filter (ord.equiv(_, pivot)),
        quickSort(seq filter (ord.gt(_, pivot))))

  def quickSortParallel[T](seq: Seq[T], threshold: Int)(using ord: Ordering[T]): Future[Seq[T]] =
    if seq.length <= 1 then Future(seq)
    else
      val pivot = seq(seq.length / 2)
      if seq.length <= threshold then
        Future(quickSort(seq))
      else
        val left = Future(quickSort(seq filter (ord.lt(_, pivot))))
        val right = Future(quickSort(seq filter (ord.gt(_, pivot))))
        left.flatMap(l => right.map(r => Seq.concat(l, seq filter (ord.equiv(_, pivot)), r)))

  def measureTime[A](f: => A, repetitions: Int = 100): Future[Duration] = {
    val start = System.nanoTime()

    val futures = (1 to repetitions).map { _ =>
      Future {
        f
      }
    }

    Future.sequence(futures).map { _ =>
      val end = System.nanoTime()
      val elapsedTime = (end - start).nanos
      elapsedTime / repetitions
    }
  }