package quickstart

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await

object Proxy extends App {
  val client: Service[Request, Response] =
    Http.newService("psnine.com:80")
  val server = Http.serve(":8080", client)
  Await.ready(server)
}
