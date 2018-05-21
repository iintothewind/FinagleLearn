package quickstart

import java.time.LocalDate

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.toggle.{Toggle, ToggleMap}
import com.twitter.finagle.util.Rng
import com.twitter.finagle.{Filter, Service, SimpleFilter}
import com.twitter.util.{Duration, Future, Timer}

class MapFilter[ReqIn, RepOut, ReqOut, RepIn](f1: ReqIn => ReqOut,
                                              f2: RepIn => RepOut
                                             )
  extends Filter[ReqIn, RepOut, ReqOut, RepIn] {
  override def apply(request: ReqIn, service: Service[ReqOut, RepIn]): Future[RepOut] =
    service(f1(request)).map(f2)
}

class DateFilter extends Filter[String, String, Int, LocalDate] {
  override def apply(request: String, service: Service[Int, LocalDate]): Future[String] =
    service(request.toInt).map(_.toString)

}

class TimeOutFilter[Req, Rep](timer: Timer,
                              timeout: Duration,
                             )
  extends Filter[Req, Rep, Req, Rep] {
  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] =
    service(request).within(timer, timeout)
}

class RetryFilter[Req, Rep](attempt: Int)
  extends SimpleFilter[Req, Rep] {
  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] =
    retry(attempt)(service(request)).getOrElse(Future.never)
}

class RetryTimeOutFilter[Req, Rep](attempt: Int)(timer: Timer, timeout: Duration)
  extends Filter[Req, Rep, Req, Rep] {
  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] =
    new RetryFilter[Req, Rep](attempt).andThen(new TimeOutFilter[Req, Rep](timer, timeout))(request, service)
}

class ExampleFilter(toggleMap: ToggleMap,
                    newBackend: Service[Request, Response])
  extends SimpleFilter[Request, Response] {

  private[this] val useNewBackend: Toggle[Int] = toggleMap("com.example.service.UseNewBackend")

  def apply(req: Request, service: Service[Request, Response]): Future[Response] = {
    if (useNewBackend(Rng.threadLocal.nextInt()))
      newBackend(req)
    else
      service(req)
  }
}