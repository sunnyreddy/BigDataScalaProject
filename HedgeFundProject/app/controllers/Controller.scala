package controllers

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import javax.inject._
import models.login.LoginHandler
import models.DAO.UserTable
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

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

@Singleton
class HomeController @Inject()(mailerClient: MailerClient)(system: ActorSystem,cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  //actors
//  val bossActor = ActorSystem().actorOf(Props[BossActor])
  implicit val timeout: Timeout = 5 minutes

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
        val handler = new LoginHandler() with UserTable
        (handler.addUser(sgd.username, sgd.password, sgd.name, sgd.email,sgd.username+"1",100000.0)).map(b => b match {
          case true => Redirect(routes.HomeController.login())
          case false => Redirect(routes.HomeController.signup()).flashing("error" -> s"**Username already exists")
      })
  })
  }

  def action(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.Action.action())
  }

  def dashboard(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.portfolio.dashboard())
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

    val email = Email("Simple email", ""+emailfrom+"", Seq(""+emailto+""), bodyText = Some("A text message"))
    mailerClient.send(email)
    Ok(s"Email  sent!")
  }
}
