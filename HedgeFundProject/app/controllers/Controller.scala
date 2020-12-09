package controllers

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import javax.inject._
import models.login.LoginHandler
import models.login.PortfolioHandler
import models.DAO.UserTable
import models.DAO.PortfolioTable
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

import play.api.data.format.Formats._
import play.api.data.format.Formats.doubleFormat


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import play.api.libs.mailer._
import java.io.File
import org.apache.commons.mail.EmailAttachment
import javax.inject.Inject

// Case class to validate login form
case class LoginForm(username: String, password: String)

// Case class to validate sign-up form
case class SignUpForm(username: String, password: String, name: String, email: String)

case class PortfolioForm(stockCode: String, quantity: Double, rule: Double)

@Singleton
class HomeController @Inject()(mailerClient: MailerClient)(system: ActorSystem,cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  //actors
//  val bossActor = ActorSystem().actorOf(Props[BossActor])
  implicit val timeout: Timeout = 5 minutes

  var user = ""
  var userEmail = ""
  var amount = 1000

  val loginData = Form(mapping(
    "Username" -> nonEmptyText,
    "Password" -> nonEmptyText
  )(LoginForm.apply)(LoginForm.unapply))

  val signupData = Form(mapping(
    "Username" -> nonEmptyText,
    "Password" -> nonEmptyText,
    "Name" -> nonEmptyText,
    "Email" -> email
  )(SignUpForm.apply)(SignUpForm.unapply))

  val portfolioData = Form(mapping(
//    "PortfolioID" -> nonEmptyText,
    "StockCode" -> nonEmptyText,
    "Quantity" -> of[Double],
    "Rule" -> of[Double]
  )(PortfolioForm.apply)(PortfolioForm.unapply))

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def login(): Action[AnyContent] = Action { implicit request =>
    //val csrfToken = CSRF.getToken.get.value
    Ok(views.html.credentials.login(loginData))//.withNewSession.withSession("csrfToken"->csrfToken)
  }

  def validateLogin(): Action[AnyContent] = Action.async { implicit request =>
    loginData.bindFromRequest.fold(
      formWithError => Future(BadRequest(views.html.credentials.login(formWithError))),
      ld => {
         user = ld.username
         val handler = new LoginHandler() with UserTable
         (handler.validateUser(ld.username, ld.password)).map(b => b match {
           case true => Redirect(routes.HomeController.dashboard())
           case false => Redirect(routes.HomeController.login()).flashing("error" -> s"**Username or password is incorrect")
      })
      })
  }

  def signup(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.credentials.signup(signupData))
  }

  def validateSignUp(): Action[AnyContent] = Action.async { implicit request =>
    signupData.bindFromRequest.fold(
      formWithError => Future(BadRequest(views.html.credentials.signup(formWithError))),
      sgd => {
        userEmail = sgd.email
        val handler = new LoginHandler() with UserTable
        (handler.addUser(sgd.username, sgd.password, sgd.name, sgd.email,sgd.username+"1",1000.0)).map(b => b match {
          case true => Redirect(routes.HomeController.login())
          case false => Redirect(routes.HomeController.signup()).flashing("error" -> s"**Username already exists")
      })
  })
  }

  def addPortfolioToDB(): Action[AnyContent] = Action.async { implicit request =>
    portfolioData.bindFromRequest.fold(
      formWithError => Future(BadRequest(views.html.portfolio.dashboard(formWithError,0))),
      sgd => {
        val handler = new PortfolioHandler() with PortfolioTable
        (handler.addPortfolio(user + "1",sgd.stockCode,100, sgd.rule.toDouble)).map(b => b match {
          case true => Redirect(routes.HomeController.dashboard())
          case false => Redirect(routes.HomeController.dashboard()).flashing("error" -> s"already have portfolio so go check ML")
        })
      })
  }

  def action(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.Action.action())
  }

  def dashboard(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.portfolio.dashboard(portfolioData,amount))
  }

//  def test(): Action[AnyContent] = Action { implicit request =>
//    Ok(views.html.test())
//  }


//  def emailSender(): Action[AnyContent] = Action { implicit request =>
//    Ok(sendEmail)
//  }
//
//  def sendEmail = {
//    val email = Email(
//      "Simple email",
//      "Mister FROM <scalatest2020@hotmail.com>",
//      Seq("Miss TO <yuan.ya@northeastern.com>"),
//      bodyText = Some("A text message")
//    )
//    mailerClient.send(email)
//  }
  def emailSender = Action {
    //  val mailer = new SMTPMailer(SMTPConfiguration("typesafe.org", 1234))
    // val id = mailer.send(Email("Simple email", "Mister FROM <abhinaykumar499@gmail.com>"))

    val emailfrom="2020hedgetest@gmail.com"
    val emailto="2020hedgetest@gmail.com"
    val subject ="Simple Email"
    val bodytext="A text message";

    val email = Email("Simple email", ""+emailfrom+"", Seq(""+userEmail+""), bodyText = Some("A text message"))
    mailerClient.send(email)
    Ok(s"Email  sent!")
  }

}
