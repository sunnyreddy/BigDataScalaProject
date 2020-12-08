
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class loginTest0 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:9000")
		.inferHtmlResources()
		

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6",
		"Cache-Control" -> "max-age=0",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.57")

	val headers_1 = Map(
		"Accept" -> "text/css,*/*;q=0.1",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6",
		"If-Modified-Since" -> "Sat, 05 Dec 2020 18:08:32 GMT",
		"If-None-Match" -> "937f1b122d95008652d7682e0d4bba6476023dc4",
		"Sec-Fetch-Dest" -> "style",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.57")


	val headers_5 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.57")

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

	val scn = scenario("users").exec(Search.search)
	setUp(scn.inject(rampUsers(500) during(5 second)).protocols(httpProtocol))
	
	object Search {
		val user = csv("users.csv").random 
		val search = 
			feed(user)
			.exec(http("LandingPage")
			.get("/")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/assets/stylesheets/main.css")
			.headers(headers_1),
            http("LoginPage")
			.get("/login?")
			.headers(headers_5)))
		.pause(5)
		.exec(http("Login_Simulate")
			.post("/validateLogin")
			.headers(headers_9)
			//.formParam("csrfToken", "${csrftoken}")
			.formParam("Username", "${username}")
			.formParam("Password", "a"))
		
	}

}