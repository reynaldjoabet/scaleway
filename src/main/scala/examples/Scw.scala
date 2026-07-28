package examples

import cats.effect.IO
import cats.effect.Resource
import cats.effect.std.Env
import sttp.client4.Backend
import sttp.client4.Request
import sttp.client4.ResponseException
import sttp.client4.http4s.Http4sBackend

/** Credentials and default scoping every example needs.
  *
  * Scaleway resources live either in a region (`fr-par`) or in one of its zones (`fr-par-1`), and every write is billed
  * against a Project inside an Organization -- so all four values show up in nearly every request.
  */
final case class ScwCredentials(
    secretKey: String,
    organizationId: String,
    projectId: String,
    region: String,
    zone: String
)

object ScwCredentials {

  private def required(name: String): IO[String] =
    Env[IO].get(name).flatMap {
      case Some(value) if value.nonEmpty => IO.pure(value)
      case _ => IO.raiseError(new IllegalStateException(s"environment variable $name is not set"))
    }

  private def withDefault(name: String, fallback: String): IO[String] =
    Env[IO].get(name).map(_.filter(_.nonEmpty).getOrElse(fallback))

  /** Reads the same variables the Scaleway CLI and Terraform provider use, so an already configured shell works as is.
    */
  val fromEnv: IO[ScwCredentials] =
    for {
      secretKey <- required("SCW_SECRET_KEY")
      organizationId <- required("SCW_DEFAULT_ORGANIZATION_ID")
      projectId <- required("SCW_DEFAULT_PROJECT_ID")
      region <- withDefault("SCW_DEFAULT_REGION", "fr-par")
      zone <- withDefault("SCW_DEFAULT_ZONE", "fr-par-1")
    } yield ScwCredentials(secretKey, organizationId, projectId, region, zone)

}

/** The two things every example needs: the credentials, and something to send requests with.
  *
  * The generated APIs only *describe* requests -- an `APIKeysApi.createAPIKey(...)` call builds a
  * `Request[Either[ResponseException[String], APIKey]]` and sends nothing. That makes them cheap to build, pass around
  * and test, but it means the effect type is chosen here, at the call site, by handing the request a backend.
  */
final class Scw(val credentials: ScwCredentials, backend: Backend[IO]) {

  /** Sends a request and collapses the two failure channels into `IO`.
    *
    * `ResponseException` covers both a non-2xx status (with the raw error body) and a body that failed to decode; both
    * extend `Exception`, so `IO.fromEither` can raise them directly. Examples that want to inspect the failure instead
    * -- to tolerate a 404, say -- should call `attempt` on the request themselves.
    */
  def run[A](request: Request[Either[ResponseException[String], A]]): IO[A] =
    request.send(backend).flatMap(response => IO.fromEither(response.body))

}

object Scw {

  /** Every Scaleway product API is served from this host; the path prefix is what differs per product. */
  val baseUrl: String = "https://api.scaleway.com"

  val resource: Resource[IO, Scw] =
    for {
      credentials <- Resource.eval(ScwCredentials.fromEnv)
      backend <- Http4sBackend.usingDefaultEmberClientBuilder[IO]()
    } yield new Scw(credentials, backend)

}

/** The Scaleway specs mark almost nothing as required on the *response* side, so every generated model field is an
  * `Option` -- including ids the API always sends back. Rather than `.get` on those, examples say which field they
  * expected, so a surprise from the API fails with something readable.
  */
extension [A](option: Option[A]) {

  def orFail(field: String): IO[A] =
    IO.fromOption(option)(new NoSuchElementException(s"the API response did not include `$field`"))

}
