package ig
  
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class IGAccessTokenSim extends Simulation {

  val concurrency: Integer = Integer.getInteger("concurrency", 10)
  val duration: Integer = Integer.getInteger("duration", 600)
  val warmup: Integer = Integer.getInteger("warmup", 1)
  val igHost: String = System.getProperty("ig_host", "openig.prod.perf.forgerock-qa.com")
  val igPort: String = System.getProperty("ig_port", "443")
  val igProtocol: String = System.getProperty("ig_protocol", "https")
  val getTokenInfo: String = System.getProperty("get_token_info", "False")
  val igUrl: String = igProtocol + "://" + igHost + ":" + igPort
  val random = new util.Random
  
  val csvFile = "/opt/CEBT/sync_folder/simulations/pavel/ig/tokens.csv"
  
  def getXOpenAMHeaders(username: String, password: String): scala.collection.immutable.Map[String, String] = {
    scala.collection.immutable.Map(
      "X-OpenAM-Username" -> username,
      "X-OpenAM-Password" -> password)
  }
  
  val httpProtocol: HttpProtocolBuilder = http
    .baseURLs(igUrl)
    .inferHtmlResources()
    .header("Accept-API-Version", "resource=2.0, protocol=1.0")
    
  val accessTokenScenario: ScenarioBuilder = scenario("IG Token Info flow")
    .during(duration) {
      feed(csv(csvFile).random)
        .exec(
          http("tokeninfo")
            .post("/rs-tokeninfo")
            .header("Authorization", "Bearer ${tokens}")
            .check(status.is(200))
        )
    }
    
  setUp(accessTokenScenario.inject(rampUsers(concurrency) over warmup)).protocols(httpProtocol)
}