
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class loginTestv3 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:9000")
		.inferHtmlResources()

	val headers_9 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6",
		"Cache-Control" -> "max-age=0",
		"Origin" -> "http://localhost:9000",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.57")

	val scn = scenario("users").exec(Test.testcase)
	setUp(scn.inject(rampUsers(1000) during(5 second)).protocols(httpProtocol))

	object Test {
		val user = csv("users.csv").random 
		val testcase = 
			feed(user)
			.exec(http("Load_LandingPage")
			.get("/"))
		.pause(1)
		.exec(http("Load_LoginPage")
			.get("/login?"))
		.pause(3)
		.exec(http("Login_Simulation")
			.post("/validateLogin")
			.headers(headers_9)
			.formParam("Username", "user0")
			.formParam("Password", "a"))
	}
}