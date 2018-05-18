package quickstart

import java.time.LocalDate

import com.twitter.finagle.{Filter, Service, SimpleFilter}
import com.twitter.util.{Duration, Future, Timer}

class MapFilter[ReqIn, RepOut, ReqOut, RepIn](f1: ReqIn => ReqOut,
                                              f2: RepIn => RepOut) extends Filter[ReqIn, RepOut, ReqOut, RepIn] {
  override def apply(request: ReqIn, service: Service[ReqOut, RepIn]): Future[RepOut] =
    service(f1(request)).map(f2)
}

class DateFilter extends Filter[String, String, Int, LocalDate] {
  override def apply(request: String, service: Service[Int, LocalDate]): Future[String] =
    service(request.toInt).map(_.toString)

}

class TimeOutFilter[Req, Rep](timer: Timer,
                              timeout: Duration,
                             ) extends Filter[Req, Rep, Req, Rep] {
  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] =
    service(request).within(timer, timeout)
}

class RetryFilter[Req, Rep](attempt: Int) extends SimpleFilter[Req, Rep] {
  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] =
    retry(attempt)(service(request)).getOrElse(Future.never)
}

class RetryTimeOutFilter[Req, Rep](attempt: Int)(timer: Timer, timeout: Duration) extends Filter[Req, Rep, Req, Rep] {
  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] =
    new TimeOutFilter[Req, Rep](timer, timeout).andThen(new RetryFilter[Req, Rep](attempt)).apply(request, service)
}