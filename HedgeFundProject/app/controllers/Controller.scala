package controllers

import akka.util.Timeout
import javax.inject._
import models.login.LoginHandler
import models.login.PortfolioHandler
import models.DAO.UserTable
import models.DAO.PortfolioTable
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import MLPackage.MajorIndexETF
import play.api.data.format.Formats._
import play.api.data.format.Formats.doubleFormat
import play.api.db._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
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
class HomeController @Inject()(mailerClient: MailerClient)(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  implicit val timeout: Timeout = 5 minutes

  var user = ""
  var userEmail = ""
  var amount = 1000
  var stockCheck = ""

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
         userEmail = Await.result(handler.getUserEmail(user), 1.second)
         (handler.validateUser(ld.username, ld.password)).map(b => b match {
           case true => Redirect(routes.HomeController.recommendation())
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
        (handler.addUser(sgd.username, sgd.password, sgd.name, sgd.email,sgd.username+"1",1000.0)).map(b => b match {
          case true => Redirect(routes.HomeController.login())
          case false => Redirect(routes.HomeController.signup()).flashing("error" -> s"**Username already exists")
      })
  })
  }

  def addPortfolioToDB(): Action[AnyContent] = Action.async { implicit request =>
    portfolioData.bindFromRequest.fold(
      formWithError => Future(BadRequest(views.html.portfolio.dashboard(formWithError,0,"0"))),
      sgd => {
        stockCheck = sgd.stockCode
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
    Ok(views.html.portfolio.dashboard(portfolioData,amount,"0"))
  }

  def getRule(stockChec : String): Action[AnyContent] = Action { implicit request =>

      val rule = MajorIndexETF.getRule(stockChec)
      if(rule != "Not Recommend")
        emailSender()

      Ok(views.html.portfolio.dashboard(portfolioData,amount,rule.toString))
  }

//  def test(): Action[AnyContent] = Action { implicit request =>
//    Ok(views.html.test())
//  }

  def emailSender():Unit = {
    val emailfrom="2020hedgetest@gmail.com"
    val emailto = userEmail
    val subject ="Simple Email"
    val bodytext="A text message";

    val email = Email(subject, ""+emailfrom+"", Seq(""+emailto+""), bodyText = Some(bodytext))

    mailerClient.send(email)
    Ok(s"Email  sent!")
  }

  def recommendation(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.portfolio.recommendation())
  }


}
