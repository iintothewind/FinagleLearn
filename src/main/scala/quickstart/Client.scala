package quickstart

import com.twitter.finagle.{Http, Service, http}
import com.twitter.util.{Await, Future}

object Client extends App {
  val client: Service[http.Request, http.Response] =
    Http
      .client
      .withTransport.httpProxyTo("google.com:443")
      .withTlsWithoutValidation
      .newService("3.20.128.6:88")
  val request = http.Request(http.Method.Get, "/")
  request.host = "www.google.com"
  val response: Future[http.Response] = client(request)
  Await.result(response.onSuccess { rep: http.Response =>
    println("GET success: " + rep)
  })
}
