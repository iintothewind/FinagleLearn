package object quickstart {
  @annotation.tailrec
  def retry[T](n: Int)(fn: => T): util.Try[T] =
    util.Try(fn) match {
      case x: util.Success[T] => x
      case _ if n > 1 => retry(n - 1)(fn)
      case otherwise => otherwise
    }
}
